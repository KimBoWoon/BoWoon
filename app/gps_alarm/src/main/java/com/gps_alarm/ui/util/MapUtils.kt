package com.gps_alarm.ui.util

import android.app.Activity
import android.content.Context
import android.location.Location
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.os.bundleOf
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.domain.gpsAlarm.dto.Addresses
import com.gps_alarm.data.Address
import com.gps_alarm.data.GpsAlarmConstant
import com.gps_alarm.ui.theme.circleOverlay
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapOptions
import com.naver.maps.map.overlay.CircleOverlay
import com.naver.maps.map.overlay.InfoWindow
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
    val density = LocalConfiguration.current
    val fusedLocationSource = FusedLocationSource(context as Activity, 1000)
    val mapView = CustomNaverMaps(
        context,
        fusedLocationSource,
        {
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
                    map = this@CustomNaverMaps
                }
            }
        },
        NaverMapOptions().apply {
            camera(CameraPosition(LatLng(address?.latitude ?: 0.0, address?.longitude ?: 0.0), 16.0))
        },
        null
    ).create()

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height((density.screenWidthDp.toFloat() * GpsAlarmConstant.SIXTEEN_BY_NINE_RATE).dp),
        factory = { mapView }
    )
}

class CustomNaverMaps(
    private val context: Context,
    private val fusedLocationSource: FusedLocationSource,
    private val naverMapSetting: (NaverMap.() -> Unit)? = null,
    private val options: NaverMapOptions? = null,
    private val locationChangeListener: (NaverMap.(Location) -> Unit)? = null,
) : DefaultLifecycleObserver {
    private val maps = MapView(context, options)
    private var infoWindow = InfoWindow().apply {
        adapter = object : InfoWindow.DefaultTextAdapter(context) {
            override fun getText(infoWindow: InfoWindow): CharSequence =
                infoWindow.marker?.tag as? CharSequence ?: ""
        }
    }

    fun setInfoWindow(adapter: InfoWindow.DefaultTextAdapter): CustomNaverMaps {
        infoWindow = InfoWindow(adapter)
        return this
    }

    fun create(): MapView =
        maps.apply {
            getMapAsync { naverMap ->
                naverMap.apply {
                    locationSource = fusedLocationSource
                    naverMapSetting?.invoke(this)

                    locationChangeListener?.let { listener ->
                        addOnLocationChangeListener {
                            listener.invoke(naverMap, it)
                        }
                    }
                }
            }
        }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        maps.onCreate(bundleOf())
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)

        maps.onStart()
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)

        maps.onResume()
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)

        maps.onPause()
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)

        maps.onStop()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)

        maps.onDestroy()
    }
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
                infoWindow?.open(marker)
            } else {
                // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                infoWindow?.close()
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