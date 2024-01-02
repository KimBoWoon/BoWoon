package com.bowoon.gps_alarm.ui.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.bowoon.gpsAlarm.R
import com.bowoon.gpsAlarm.databinding.GpsAlarmActivityBinding
import com.bowoon.gps_alarm.base.BaseActivity
import com.bowoon.gps_alarm.ui.viewmodel.GpsAlarmVM
import dagger.hilt.android.AndroidEntryPoint

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
}