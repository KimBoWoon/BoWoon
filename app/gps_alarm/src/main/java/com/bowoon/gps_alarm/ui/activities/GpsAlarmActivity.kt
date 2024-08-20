package com.bowoon.gps_alarm.ui.activities

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.bowoon.backstack.AppDoubleBackToExit
import com.bowoon.backstack.AppDoubleBackToExitEvent
import com.bowoon.backstack.Backstack
import com.bowoon.commonutils.Log
import com.bowoon.gpsAlarm.R
import com.bowoon.gpsAlarm.databinding.GpsAlarmActivityBinding
import com.bowoon.gps_alarm.base.BaseActivity
import com.bowoon.gps_alarm.ui.fragments.AlarmFragment
import com.bowoon.gps_alarm.ui.fragments.MapsFragment
import com.bowoon.gps_alarm.ui.fragments.SettingFragment
import com.bowoon.gps_alarm.ui.fragments.vm.GpsAlarmVM
import com.bowoon.permissionmanager.requestMultiplePermission
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class GpsAlarmActivity : BaseActivity() {
    companion object {
        private const val TAG = "#GpsAlarmActivity"
    }

    private val binding: GpsAlarmActivityBinding by lazy {
        DataBindingUtil.setContentView(this@GpsAlarmActivity, R.layout.gps_alarm_activity)
    }
    private val viewModel by viewModels<GpsAlarmVM>()
    private val alarmFragment = AlarmFragment.newInstance()
    private val mapsFragment = MapsFragment.newInstance()
    private val settingFragment = SettingFragment.newInstance()

    @Inject
    lateinit var appDoubleBackToExitFactory: AppDoubleBackToExit.AppDoubleBackToExitFactory
    private val appDoubleBackToExit by lazy {
        appDoubleBackToExitFactory.create(
            R.string.app_double_back_to_exit_msg,
            1500
        )
    }

    @Inject
    lateinit var backstackFactory: Backstack.BackstackFactory
    private val backstack by lazy {
        backstackFactory.create(R.id.nav_alarm)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            appDoubleBackToExit.event.collect { event ->
                when (event) {
                    AppDoubleBackToExitEvent.One -> Log.d(TAG, "onBackPressed one")
                    AppDoubleBackToExitEvent.Two -> Log.d(TAG, "onBackPressed two")
                    AppDoubleBackToExitEvent.Exit -> finish()
                }
            }
        }

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (backstack.isEmpty()) {
                        if (binding.bnvGpsAlarmNavigation.selectedItemId != R.id.nav_alarm) {
                            backstack.clear()
                            binding.bnvGpsAlarmNavigation.selectedItemId = R.id.nav_alarm
                        } else {
//                            appDoubleBackToExit.onBackPressed { finish() }
                            lifecycleScope.launch { appDoubleBackToExit.onBackPressed() }
                        }
                    } else {
                        backstack.remove()
                        binding.bnvGpsAlarmNavigation.selectedItemId = backstack.peek()
                    }
                }
            }
        )

        binding.apply {
            lifecycleOwner = this@GpsAlarmActivity
        }

        initNavigation()

        binding.bnvGpsAlarmNavigation.setOnItemSelectedListener {
            backstack.add(it.itemId)
            changeFragment(getFragment(it.itemId))
            true
        }

        requestMultiplePermission(
            { Log.d(TAG, "all granted") },
            { Log.d(TAG, "all denied") }
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

    fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fcv_content, fragment)
            .commitAllowingStateLoss()
    }

    fun getFragment(id: Int): Fragment =
        when (id) {
            R.id.nav_alarm -> alarmFragment
            R.id.nav_maps -> mapsFragment
            R.id.nav_setting -> settingFragment
            else -> alarmFragment
        }
}