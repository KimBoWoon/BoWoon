package com.bowoon.gps_alarm.ui.map

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bowoon.commonutils.DataStatus
import com.bowoon.commonutils.Log
import com.bowoon.commonutils.ifNotNull
import com.bowoon.gpsAlarm.R
import com.bowoon.gpsAlarm.databinding.MapsFragmentBinding
import com.bowoon.gps_alarm.base.BaseFragment
import com.bowoon.gps_alarm.data.SettingInfo
import com.bowoon.gps_alarm.ui.viewmodel.MapVM
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.overlay.CircleOverlay
import com.naver.maps.map.overlay.Marker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapsFragment : BaseFragment() {
    companion object {
        private const val TAG = "#MapsFragment"
    }

    private lateinit var binding: MapsFragmentBinding
    private val viewModel by viewModels<MapVM>()
    private val markerList = mutableListOf<Marker>()
    private val circleList = mutableListOf<CircleOverlay>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.maps_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = this@MapsFragment
        }

        initBinding()
        initFlow()
    }

    override fun onResume() {
        super.onResume()

        markerList.forEach {
            it.map = null
        }
        markerList.clear()
        circleList.forEach {
            it.map = null
        }
        circleList.clear()
        viewModel.fetchSetting()
    }

    override fun initBinding() {

    }

    override fun initFlow() {
        lifecycleScope.launch {
            viewModel.alarmList.collectLatest {
                when (it) {
                    is DataStatus.Loading -> {
                        Log.d(TAG, "alarm data loading...")
                    }
                    is DataStatus.Success -> {
                        binding.mapView.getMapAsync { naverMap ->
                            it.data?.forEach { address ->
                                ifNotNull(address.latitude, address.longitude) { latitude, longitude ->
                                    LatLng(latitude, longitude).also { latlng ->
                                        Marker(latlng).apply {
                                            markerList.add(this)
                                            position = latlng
                                            map = naverMap
                                        }
                                        CircleOverlay(
                                            latlng,
                                            (viewModel.setting.value as? DataStatus.Success<SettingInfo?>)?.data?.circleSize?.toDouble() ?: 0.0
                                        ).apply {
                                            circleList.add(this)
                                            color = Color.parseColor("#800000FF")
                                            map = naverMap
                                        }
                                    }
                                }
                            }

                            naverMap.apply {
                                locationTrackingMode = LocationTrackingMode.None
                                isIndoorEnabled = true
                                uiSettings.apply {
                                    isZoomControlEnabled = false
                                    isLocationButtonEnabled = false
                                }
                            }
                        }
                    }
                    is DataStatus.Failure -> {
                        Log.printStackTrace(it.throwable)
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.setting.collectLatest {
                when (it) {
                    is DataStatus.Loading -> {
                        Log.d(TAG, "data loading...")
                    }
                    is DataStatus.Success -> {

                    }
                    is DataStatus.Failure -> {
                        Log.printStackTrace(it.throwable)
                    }
                }
            }
        }
    }
}