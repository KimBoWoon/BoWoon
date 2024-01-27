package com.bowoon.gps_alarm.ui.alarm

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bowoon.commonutils.ContextUtils.showToast
import com.bowoon.commonutils.Log
import com.bowoon.commonutils.getSafetyParcelableExtra
import com.bowoon.gpsAlarm.R
import com.bowoon.gpsAlarm.databinding.CreateAlarmFragmentBinding
import com.bowoon.gps_alarm.base.BaseFragment
import com.bowoon.gps_alarm.data.Geocode
import com.bowoon.gps_alarm.data.Week
import com.bowoon.gps_alarm.ui.viewmodel.AlarmVM
import com.bowoon.gps_alarm.webview.AddressWebViewActivity
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.overlay.Marker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateAlarmFragment : BaseFragment() {
    companion object {
        private const val TAG = "#CreateAlarmFragment"

        const val RESULT_ADDRESS = "ADDRESS"
        const val ADDRESS_DATA_RESULT_CODE = 1000
    }

    private lateinit var binding: CreateAlarmFragmentBinding
    private val viewModel by viewModels<AlarmVM>()
    private val handler by lazy { ClickHandler() }
    private var geocode: Geocode? = null
    private val activityResultCallback = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == ADDRESS_DATA_RESULT_CODE) {
            binding.tvResultAddress.isVisible = true
            binding.mapView.isVisible = true
            binding.includeWeek.root.isVisible = true
            geocode = result?.data?.getSafetyParcelableExtra<Geocode>(RESULT_ADDRESS)
            binding.mapView.getMapAsync { naverMap ->
                LatLng(geocode?.addresses?.firstOrNull()?.latitude ?: 0.0, geocode?.addresses?.firstOrNull()?.longitude ?: 0.0).also {
                    naverMap.moveCamera(CameraUpdate.scrollTo(it))
                    Marker().apply {
                        position = it
                        map = naverMap
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CreateAlarmFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = this@CreateAlarmFragment
            handler = this@CreateAlarmFragment.handler
        }

        initBinding()
        initFlow()
    }

    override fun initBinding() {
        binding.apply {
        }
    }

    override fun initFlow() {

    }

    inner class ClickHandler {
        fun openAddressWebView() {
            activityResultCallback.launch(Intent(requireContext(), AddressWebViewActivity::class.java))
        }

        fun saveAddress() {
            Log.d(TAG, geocode.toString())
            if (binding.etAlarmTitle.text.toString().trim().isEmpty()) {
                requireContext().showToast("알림 제목을 입력해주세요.", Toast.LENGTH_SHORT)
            } else {
                viewModel.addAlarm(binding.etAlarmTitle.text.toString(), geocode?.addresses?.firstOrNull())
                findNavController().popBackStack()
            }
        }

        fun onWeek(day: String) {
            Week.valueOf(day).also {
                val backgroundResource = ResourcesCompat.getDrawable(resources, R.drawable.choose_day_bg, null)

                if (viewModel.week.contains(it)) {
                    viewModel.week.remove(it)
                } else {
                    viewModel.week.add(it)
                }

                when (it) {
                    Week.MONDAY -> binding.includeWeek.tvMonday
                    Week.TUESDAY -> binding.includeWeek.tvTuesday
                    Week.WEDNESDAY -> binding.includeWeek.tvWednesday
                    Week.THURSDAY -> binding.includeWeek.tvThursday
                    Week.FRIDAY -> binding.includeWeek.tvFriday
                    Week.SATURDAY -> binding.includeWeek.tvSaturday
                    Week.SUNDAY -> binding.includeWeek.tvSunday
                }.run {
                    background = if (!viewModel.week.contains(it)) {
                        null
                    } else {
                        backgroundResource
                    }
                }
            }
        }
    }
}