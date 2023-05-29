package com.gps_alarm.ui.util

import android.app.Activity
import android.os.Bundle
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.domain.gpsAlarm.dto.Addresses
import com.gps_alarm.data.Address
import com.gps_alarm.data.GpsAlarmConstant
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMapOptions
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.coroutines.launch

@Composable
fun FixedMarkerMap(address: Addresses?) {
    address?.let {
        FixedMarkerMap(address = dataMapper("", it))
    }
}

@Composable
fun FixedMarkerMap(address: Address?) {
    val context = LocalContext.current
    val density = LocalConfiguration.current
    val fusedLocationSource = FusedLocationSource(context as Activity, 1000)
    val mapView = remember {
        val naverMapOptions = NaverMapOptions().apply {
            camera(CameraPosition(LatLng(address?.latitude ?: 0.0, address?.longitude ?: 0.0), 16.0))
        }
        MapView(context, naverMapOptions).apply {
            getMapAsync { naverMap ->
                naverMap.apply {
                    locationSource = fusedLocationSource
                    locationTrackingMode = LocationTrackingMode.None
                    isIndoorEnabled = true
                    minZoom = 16.0
                    maxZoom = 16.0
                    uiSettings.apply {
                        isLocationButtonEnabled = false
                        isScrollGesturesEnabled = false
                    }

                    if (address?.latitude != null && address.longitude != null) {
                        Marker().apply {
                            position = LatLng(address.latitude, address.longitude)
                            width = Marker.SIZE_AUTO
                            height = Marker.SIZE_AUTO
                            map = naverMap
                        }
                    }
                }
            }
        }
    }
    OnLifecycleEvent { source, event ->
        source.lifecycleScope.launch {
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

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height((density.screenWidthDp.toFloat() * GpsAlarmConstant.SIXTEEN_BY_NINE_RATE).dp),
        factory = { mapView }
    )
}