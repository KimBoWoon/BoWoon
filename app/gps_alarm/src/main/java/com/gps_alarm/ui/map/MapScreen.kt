package com.gps_alarm.ui.map

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
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
import com.gps_alarm.data.Address
import com.gps_alarm.ui.activities.GpsAlarmActivity
import com.gps_alarm.ui.theme.circleOverlay
import com.gps_alarm.ui.viewmodel.MapVM
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.overlay.CircleOverlay
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.coroutines.launch
import util.DataStatus
import util.Log

@Composable
fun MapScreen(onNavigate: NavHostController) {
    MapsCompose()
}

@Composable
fun MapsCompose() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()
    val viewModel: MapVM = hiltViewModel()
    var sendNoti by remember { mutableStateOf(false) }
    var notificationId by remember { mutableStateOf<Int?>(null) }
    val fusedLocationSource = FusedLocationSource(context as Activity, 1000)
    val mapView = remember {
        MapView(context).apply {
            getMapAsync { naverMap ->
                val myLocation = Location("")
                val markerLocations = mutableListOf<Location>()
                val infoWindow = InfoWindow().apply {
                    adapter = object : InfoWindow.DefaultTextAdapter(context) {
                        override fun getText(infoWindow: InfoWindow): CharSequence {
                            return infoWindow.marker?.tag as? CharSequence ?: ""
                        }
                    }
                }

                naverMap.apply {
                    locationSource = fusedLocationSource
                    locationTrackingMode = LocationTrackingMode.Follow
                    isIndoorEnabled = true
                    uiSettings.apply {
                        isLocationButtonEnabled = true
                        isScrollGesturesEnabled = true
                    }

                    viewModel.getAddress()
                    coroutineScope.launch {
                        viewModel.addressList.collect {
                            when (it) {
                                is DataStatus.Loading -> {
                                    Log.d("addressList Loading...")
                                }
                                is DataStatus.Success -> {
                                    it.data.forEach { address ->
                                        if (address.latitude != null && address.longitude != null) {
                                            addMarker(context, infoWindow, address).apply {
                                                map = if (address.isEnable == true) {
                                                    naverMap
                                                } else {
                                                    null
                                                }
                                            }
                                            addCircleOverlay(address).apply {
                                                map = if (address.isEnable == true) {
                                                    naverMap
                                                } else {
                                                    null
                                                }
                                            }
                                            markerLocations.add(Location("").apply {
                                                latitude = address.latitude
                                                longitude = address.longitude
                                            })
                                        }
                                    }
                                }
                                is DataStatus.Failure -> {
                                    Log.printStackTrace(it.throwable)
                                }
                            }
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
                            if (myLocation.distanceTo(markerLocation) <= 50 && notificationId == null) {
                                notificationId = 0
                                sendNoti = true
                            }
//                            if (myLocation.distanceTo(markerLocation) > 50 && notificationId != null) {
//                                notificationId = null
//                                sendNoti = true
//                            }
                        }
//                        moveCamera(CameraUpdate.scrollTo(coord))
//                        moveCamera(CameraUpdate.zoomTo(16.0))
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
        SendNotification(notificationId)
    }

    // 생성된 MapView 객체를 AndroidView로 Wrapping 합니다.
    AndroidView(factory = { mapView })
}

fun addMarker(context: Context, infoWindow: InfoWindow, address: Address): Marker = Marker().apply {
    if (address.isEnable == true && address.latitude != null && address.longitude != null) {
        position = LatLng(address.latitude, address.longitude)
        width = Marker.SIZE_AUTO
        height = Marker.SIZE_AUTO
        tag = address.name
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
    }
}

fun addCircleOverlay(address: Address): CircleOverlay = CircleOverlay().apply {
    if (address.isEnable == true && address.latitude != null && address.longitude != null) {
        center = LatLng(address.latitude, address.longitude)
        radius = 100.0
        color = circleOverlay.toArgb()
    }
}

@Composable
fun SendNotification(notificationId: Int?) {
    val context = LocalContext.current
    val CHANNEL_ID = context.getString(R.string.notification_channel_id)
    var message = "목적지를 확인하세요."

    // Create an explicit intent for an Activity in your app
    val intent = Intent(context, GpsAlarmActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

    val notification = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_background)
        .setContentTitle("GPS 알람")
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        // Set the intent that will fire when the user taps the notification
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .build().apply {
            flags = NotificationCompat.FLAG_INSISTENT
        }

    with(NotificationManagerCompat.from(context)) {
        // notificationId is a unique int for each notification that you must define
        notify(notificationId ?: 0, notification)
    }
}