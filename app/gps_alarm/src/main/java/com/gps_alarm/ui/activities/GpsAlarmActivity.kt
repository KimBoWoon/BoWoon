package com.gps_alarm.ui.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Surface
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.gps_alarm.base.BaseActivity
import com.gps_alarm.service.GpsAlarmService
import com.gps_alarm.ui.main.GpsMainCompose
import com.gps_alarm.ui.theme.GpsAlarmTheme
import com.gps_alarm.ui.viewmodel.GpsAlarmVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import util.Log

@AndroidEntryPoint
class GpsAlarmActivity : BaseActivity() {
    private val TAG = this.javaClass.simpleName
    private val viewModel by viewModels<GpsAlarmVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startService()

        setContent {
            GpsAlarmTheme {
                Surface {
                    GpsMainCompose()
                }
            }
        }
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