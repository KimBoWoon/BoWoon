package com.gps_alarm.ui.util

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.domain.gpsAlarm.dto.Addresses
import com.gps_alarm.data.Address
import com.gps_alarm.ui.map.CustomNaverMaps
import com.gps_alarm.ui.theme.circleOverlay
import com.gps_alarm.ui.theme.getSixteenByNineSize
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMapOptions
import com.naver.maps.map.overlay.CircleOverlay
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.util.FusedLocationSource

@Composable
fun FixedMarkerMap(address: Addresses?) {
    address?.let {
        FixedMarkerMap(address = dataMapper("", it))
    }
}

@Composable
fun FixedMarkerMap(address: Address?) {
    val context = LocalContext.current
    val fusedLocationSource = FusedLocationSource(context as Activity, 1000)
    val mapView = CustomNaverMaps(
        context,
        NaverMapOptions().apply {
            camera(CameraPosition(LatLng(address?.latitude ?: 0.0, address?.longitude ?: 0.0), 16.0))
        }
    ).setMapSettings {
        it.locationTrackingMode = LocationTrackingMode.None
        it.isIndoorEnabled = true
        it.minZoom = 16.0
        it.maxZoom = 16.0
        it.uiSettings.apply {
            isLocationButtonEnabled = false
            isScrollGesturesEnabled = false
        }

        if (address?.latitude != null && address.longitude != null) {
            Marker().apply {
                position = LatLng(address.latitude, address.longitude)
                width = Marker.SIZE_AUTO
                height = Marker.SIZE_AUTO
                map = it
            }
        }
    }.create()

    AndroidView(
        modifier = Modifier.getSixteenByNineSize(),
        factory = { mapView }
    )
}

fun addMarker(address: Address): Marker = Marker().apply {
    if (address.isEnable == true && address.latitude != null && address.longitude != null) {
        position = LatLng(address.latitude, address.longitude)
        width = Marker.SIZE_AUTO
        height = Marker.SIZE_AUTO
        tag = address.name
        onClickListener = Overlay.OnClickListener { overlay ->
            val marker = overlay as Marker

            if (marker.infoWindow == null) {
                // 현재 마커에 정보 창이 열려있지 않을 경우 엶
                CustomNaverMaps.infoWindow?.open(marker)
            } else {
                // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                CustomNaverMaps.infoWindow?.close()
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