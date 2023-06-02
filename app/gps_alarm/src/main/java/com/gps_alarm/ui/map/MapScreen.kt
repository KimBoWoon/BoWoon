package com.gps_alarm.ui.map

import android.app.Activity
import android.location.Location
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.gps_alarm.ui.util.CustomNaverMaps
import com.gps_alarm.ui.util.SendNotification
import com.gps_alarm.ui.util.addCircleOverlay
import com.gps_alarm.ui.util.addMarker
import com.gps_alarm.ui.viewmodel.MapVM
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.util.FusedLocationSource
import util.Log

@Composable
fun MapScreen(onNavigate: NavHostController) {
    val context = LocalContext.current
    val viewModel = hiltViewModel<MapVM>()
    var sendNoti by remember { mutableStateOf(false) }
    val notificationId by remember { mutableStateOf<Int?>(null) }
    val fusedLocationSource = FusedLocationSource(context as Activity, 1000)
    val lifecycle = LocalLifecycleOwner.current
    val mapView = CustomNaverMaps(
        context,
        fusedLocationSource,
        {
            locationTrackingMode = LocationTrackingMode.Follow
            isIndoorEnabled = true
            uiSettings.apply {
                isLocationButtonEnabled = true
                isScrollGesturesEnabled = true
            }
        },
        null,
        { location ->
            val myLocation = Location("")
            val coord = LatLng(location)
            locationOverlay.apply {
                isVisible = true
                position = coord
                bearing = location.bearing
            }
            myLocation.latitude = location.latitude
            myLocation.longitude = location.longitude
            moveCamera(CameraUpdate.scrollTo(coord))
            moveCamera(CameraUpdate.zoomTo(16.0))
        }
    ).setInfoWindow(
        object : InfoWindow.DefaultTextAdapter(context) {
            override fun getText(infoWindow: InfoWindow): CharSequence {
                return infoWindow.marker?.tag as? CharSequence ?: ""
            }
        }
    ).create()

    LaunchedEffect("key") {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.fetchAlarmList()

            viewModel.container.stateFlow.flowWithLifecycle(lifecycle.lifecycle, Lifecycle.State.STARTED).collect { state ->
                when {
                    state.loading -> {
                        Log.d("addressList Loading...")
                    }
                    state.alarmList.isEmpty() && state.error == null -> {
                    }
                    state.alarmList.isNotEmpty() && state.error == null -> {
                        mapView.getMapAsync { naverMap ->
                            state.alarmList.forEach { address ->
                                addMarker(address).apply {
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
                            }
                        }
                    }
                    else -> {
                        Log.printStackTrace(state.error)
                    }
                }
            }
        }
    }

    if (sendNoti) {
        sendNoti = false
        SendNotification(notificationId)
    }

    AndroidView(factory = { mapView })
}