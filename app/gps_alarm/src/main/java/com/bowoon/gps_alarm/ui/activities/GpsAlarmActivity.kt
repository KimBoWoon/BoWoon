package com.bowoon.gps_alarm.ui.activities

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.bowoon.commonutils.Log
import com.bowoon.gpsAlarm.R
import com.bowoon.gpsAlarm.databinding.GpsAlarmActivityBinding
import com.bowoon.gps_alarm.base.BaseActivity
import com.bowoon.gps_alarm.ui.viewmodel.GpsAlarmVM
import com.bowoon.permissionmanager.requestMultiplePermission
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GpsAlarmActivity : BaseActivity() {
    companion object {
        private const val TAG = "#GpsAlarmActivity"
    }

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

        requestMultiplePermission(
            {
                Log.d(TAG, "all granted")
            },
            {
                Log.d(TAG, "all denied")
            }
        ).launch(needPermission())
    }

    private fun initNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fcv_content) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bnvGpsAlarmNavigation, navController)
    }

    private fun needPermission(): Array<String> {
        val requestPermissionList = mutableListOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionList.add(Manifest.permission.POST_NOTIFICATIONS)
//            requestPermissionList.add(Manifest.permission.USE_EXACT_ALARM)
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            requestPermissionList.add(Manifest.permission.SCHEDULE_EXACT_ALARM)
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            requestPermissionList.add(Manifest.permission.FOREGROUND_SERVICE)
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            requestPermissionList.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
//        }

        return requestPermissionList.toTypedArray()
    }
}