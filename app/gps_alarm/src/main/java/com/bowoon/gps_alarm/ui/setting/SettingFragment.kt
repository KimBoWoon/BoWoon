package com.bowoon.gps_alarm.ui.setting

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bowoon.commonutils.DataStatus
import com.bowoon.commonutils.Log
import com.bowoon.gpsAlarm.R
import com.bowoon.gpsAlarm.databinding.SettingFragmentBinding
import com.bowoon.gps_alarm.base.BaseFragment
import com.bowoon.gps_alarm.data.SettingInfo
import com.bowoon.gps_alarm.ui.viewmodel.SettingVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingFragment : BaseFragment() {
    companion object {
        private const val TAG = "#SettingFragment"
    }

    private lateinit var binding: SettingFragmentBinding
    private val handler by lazy { ClickHandler() }
    private val viewModel by viewModels<SettingVM>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.setting_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = this@SettingFragment
            handler = this@SettingFragment.handler
        }

        initBinding()
        initFlow()
    }

    override fun onResume() {
        super.onResume()

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            binding.tvPermissionTitle.text = "권한 허용"
            binding.bGoToSetting.isVisible = false
        } else {
            binding.tvPermissionTitle.text = "권한 거부"
            binding.bGoToSetting.isVisible = true
        }
    }

    override fun initBinding() {

    }

    override fun initFlow() {
        lifecycleScope.launch {
            viewModel.setting.collectLatest {
                when (it) {
                    is DataStatus.Loading -> {
                        Log.d(TAG, "setting data loading...")
                    }
                    is DataStatus.Success -> {
                        binding.etCircleArea.setText(it.data?.circleSize.toString())
                    }
                    is DataStatus.Failure -> {
                        Log.printStackTrace(it.throwable)
                    }
                }
            }
        }
    }

    inner class ClickHandler {
        fun goToSetting() {
            startActivity(
                Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:${requireContext().packageName}")
                )
            )
        }

        fun saveSetting() {
            val circleSize = if (binding.etCircleArea.text.toString().isNotEmpty()) {
                binding.etCircleArea.text.toString().toInt()
            } else {
                0
            }

            viewModel.saveSetting(
                SettingInfo(
                    circleSize = circleSize
                )
            )
        }
    }
}