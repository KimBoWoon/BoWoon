package com.bowoon.gps_alarm.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.bowoon.commonutils.ifNotNull
import com.bowoon.gpsAlarm.databinding.NaverMapViewBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.overlay.Marker

class FixedNaverMapView @JvmOverloads constructor(
    private val context: Context,
    private val attrs: AttributeSet? = null,
    private val defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    companion object {
        private const val TAG = "#FixedNaverMapView"
    }

    private val binding by lazy {
        NaverMapViewBinding.inflate(LayoutInflater.from(context), this, true)
    }
    var latitude: Double? = null
    var longitude: Double? = null

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        binding.mapView.getMapAsync { naverMap ->
            ifNotNull(latitude, longitude) { latitude, longitude ->
                LatLng(latitude, longitude).also {
                    naverMap.moveCamera(CameraUpdate.scrollTo(it))
                    Marker().apply {
                        position = it
                        map = naverMap
                    }
                }
            }
            naverMap.minZoom = 16.0
            naverMap.maxZoom = 16.0
            naverMap.locationTrackingMode = LocationTrackingMode.None
            naverMap.isIndoorEnabled = true
            naverMap.uiSettings.apply {
                isZoomControlEnabled = false
                isLocationButtonEnabled = false
                isScrollGesturesEnabled = false
            }
        }
    }
}