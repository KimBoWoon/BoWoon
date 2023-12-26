package com.bowoon.gps_alarm.ui.alarm

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bowoon.gpsAlarm.databinding.CreateAlarmFragmentBinding
import com.bowoon.gps_alarm.base.BaseFragment
import com.bowoon.gps_alarm.ui.util.getSafetyParcelableExtra
import com.bowoon.gps_alarm.ui.viewmodel.AlarmVM
import com.bowoon.gps_alarm.webview.AddressWebViewActivity
import com.data.util.Log
import com.domain.gpsAlarm.dto.Geocode
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateAlarmFragment : BaseFragment() {
    companion object {
        private const val TAG = "#CreateAlarmFragment"
    }

    private lateinit var binding: CreateAlarmFragmentBinding
    private val viewModel by viewModels<AlarmVM>()
    private val handler by lazy { ClickHandler() }
    private var geocode: Geocode? = null
    private val activityResultCallback = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == 1000) {
            geocode = result?.data?.getSafetyParcelableExtra<Geocode>("address")
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
            viewModel.addAlarm("CreateAlarm", geocode?.addresses?.firstOrNull())
            findNavController().popBackStack()
        }
    }
}