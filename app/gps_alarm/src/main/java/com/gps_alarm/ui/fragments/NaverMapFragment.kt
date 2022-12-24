package com.gps_alarm.ui.fragments

import android.os.Bundle
import android.view.View
import com.bowoon.android.gps_alarm.R
import com.bowoon.android.gps_alarm.databinding.FragmentNaverMapBinding
import com.gps_alarm.base.BaseFragment
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.util.FusedLocationSource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NaverMapFragment : BaseFragment<FragmentNaverMapBinding>(
    R.layout.fragment_naver_map
) {
    companion object {
        fun newInstance(): NaverMapFragment = NaverMapFragment()
    }

    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBinding()
        initFlow()
    }

    override fun initBinding() {
        binding.apply {
            locationSource = FusedLocationSource(this@NaverMapFragment, 1000)
            map.getFragment<MapFragment>().getMapAsync { naverMap ->
                this@NaverMapFragment.naverMap = naverMap.apply {
                    locationSource = this@NaverMapFragment.locationSource
                    locationTrackingMode = LocationTrackingMode.Follow
                    uiSettings.isLocationButtonEnabled = true
                    addOnLocationChangeListener { location ->
                        val coord = LatLng(location)
                        locationOverlay.apply {
                            isVisible = true
                            position = coord
                            bearing = location.bearing
                        }
                        moveCamera(CameraUpdate.scrollTo(coord))
                        moveCamera(CameraUpdate.zoomTo(16.0))
                    }
                }
            }
        }
    }

    override fun initFlow() {

    }
}