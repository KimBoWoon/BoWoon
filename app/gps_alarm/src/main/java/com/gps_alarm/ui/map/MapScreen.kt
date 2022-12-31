package com.gps_alarm.ui.map

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.Vibrator
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import com.bowoon.android.gps_alarm.R
import com.gps_alarm.ui.activities.GpsAlarmActivity
import com.gps_alarm.ui.theme.circleOverlay
import com.gps_alarm.ui.viewmodel.MapVM
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.overlay.CircleOverlay
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.coroutines.launch

@Composable
fun MapScreen(onNavigate: NavHostController) {
    MapsCompose()
}

@Composable
fun MapsCompose() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()
    var sendNoti by remember { mutableStateOf(false) }
    val viewModel: MapVM = hiltViewModel()
    val mapView = remember {
        MapView(context).apply {
            getMapAsync { naverMap ->
                val fusedLocationSource = FusedLocationSource(context as Activity, 1000)
                val infoWindow = InfoWindow()
                infoWindow.adapter = object : InfoWindow.DefaultTextAdapter(context) {
                    override fun getText(infoWindow: InfoWindow): CharSequence {
                        return infoWindow.marker?.tag as? CharSequence ?: ""
                    }
                }
                val myLocation = Location("")
                val markerLocations = mutableListOf<Location>()
                naverMap.apply {
                    locationSource = fusedLocationSource
                    locationTrackingMode = LocationTrackingMode.Follow
                    uiSettings.isLocationButtonEnabled = true
                    viewModel.addressList.value.forEach {
                        if (it.latitude != null && it.longitude != null) {
                            Marker().apply {
                                position = LatLng(it.latitude, it.longitude)
                                width = Marker.SIZE_AUTO
                                height = Marker.SIZE_AUTO
                                tag = it.name
                                onClickListener = Overlay.OnClickListener { overlay ->
                                    val marker = overlay as Marker

                                    if (marker.infoWindow == null) {
                                        // 현재 마커에 정보 창이 열려있지 않을 경우 엶
                                        infoWindow.open(marker)
                                    } else {
                                        // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                                        infoWindow.close()
                                    }

                                    true
                                }
                                map = naverMap
                            }
                            CircleOverlay().apply {
                                center = LatLng(it.latitude, it.longitude)
                                radius = 100.0
                                color = circleOverlay.toArgb()
                                map = naverMap
                            }
                            markerLocations.add(Location("").apply {
                                latitude = it.latitude
                                longitude = it.longitude
                            })
                        }
                    }
                    addOnLocationChangeListener { location ->
                        val coord = LatLng(location)
                        locationOverlay.apply {
                            isVisible = true
                            position = coord
                            bearing = location.bearing
                        }
                        myLocation.latitude = location.latitude
                        myLocation.longitude = location.longitude
                        markerLocations.forEach { markerLocation ->
                            if (myLocation.distanceTo(markerLocation) <= 50) {
                                sendNoti = true
                            }
                        }
                        moveCamera(CameraUpdate.scrollTo(coord))
                        moveCamera(CameraUpdate.zoomTo(16.0))
                    }
                }
            }
        }
    }
    val lifecycleObserver = remember {
        LifecycleEventObserver { source, event ->
            // CoroutineScope 안에서 호출해야 정상적으로 동작합니다.
            coroutineScope.launch {
                when (event) {
                    Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
                    Lifecycle.Event.ON_START -> mapView.onStart()
                    Lifecycle.Event.ON_RESUME -> mapView.onResume()
                    Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                    Lifecycle.Event.ON_STOP -> mapView.onStop()
                    Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                    Lifecycle.Event.ON_ANY -> TODO()
                }
            }
        }
    }

    // 뷰가 해제될 때 이벤트 리스너를 제거합니다.
    DisposableEffect(true) {
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
        }
    }

    if (sendNoti) {
        sendNoti = false
        SendNotification()
    }

    // 생성된 MapView 객체를 AndroidView로 Wrapping 합니다.
    AndroidView(factory = { mapView })
}

@Composable
fun SendNotification() {
    val CHANNEL_ID = "gpsAlarm"
    val notificationId = 0
    val context = LocalContext.current

    // Create an explicit intent for an Activity in your app
    val intent = Intent(context, GpsAlarmActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_background)
        .setContentTitle("목적지에 거의 도착했습니다.")
        .setContentText("목적지를 확인하세요!")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        // Set the intent that will fire when the user taps the notification
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    with(NotificationManagerCompat.from(context)) {
        // notificationId is a unique int for each notification that you must define
        notify(notificationId, builder.build())
    }
}