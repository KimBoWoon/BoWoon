package com.bowoon.gps_alarm.ui.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.bowoon.gpsAlarm.R
import com.bowoon.gpsAlarm.databinding.GpsAlarmActivityBinding
import com.bowoon.gps_alarm.base.BaseActivity
import com.bowoon.gps_alarm.service.GpsAlarmService
import com.bowoon.gps_alarm.ui.viewmodel.GpsAlarmVM
import com.data.util.Log
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GpsAlarmActivity : BaseActivity() {
    private val TAG = this.javaClass.simpleName
    private val binding: GpsAlarmActivityBinding by lazy {
        DataBindingUtil.setContentView(this@GpsAlarmActivity, R.layout.gps_alarm_activity)
    }
    private val viewModel by viewModels<GpsAlarmVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            lifecycleOwner = this@GpsAlarmActivity
        }

        initNavigation()
    }

    private fun initNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fcv_content) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bnvGpsAlarmNavigation, navController)
    }

    private fun startService() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.container.stateFlow.collect {
                    if (ActivityCompat.checkSelfPermission(this@GpsAlarmActivity, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Intent(this@GpsAlarmActivity, GpsAlarmService::class.java).apply {
                            action = "StartService"
                            putExtra(GpsAlarmService.ADDRESS_LIST, it.alarmList?.toTypedArray())
                            putExtra(GpsAlarmService.SETTING_INFO, it.settingInfo)
                        }.run {
                            when {
                                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> this@GpsAlarmActivity.startForegroundService(this)
                                else -> this@GpsAlarmActivity.startService(this)
                            }
                        }
                    } else {
                        Log.e("ACCESS_BACKGROUND_LOCATION DENIED")
                    }
                }
            }
        }
    }
}