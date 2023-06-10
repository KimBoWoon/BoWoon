package com.gps_alarm.ui.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Surface
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.gps_alarm.base.BaseActivity
import com.gps_alarm.service.GpsAlarmWorkManager
import com.gps_alarm.service.GpsAlarmWorkManager.Companion.ADDRESS_LIST
import com.gps_alarm.ui.main.GpsMainCompose
import com.gps_alarm.ui.theme.GpsAlarmTheme
import com.gps_alarm.ui.viewmodel.GpsAlarmVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class GpsAlarmActivity : BaseActivity() {
    private val TAG = this.javaClass.simpleName
    private val viewModel by viewModels<GpsAlarmVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createWorkManager()

        setContent {
            GpsAlarmTheme {
                Surface {
                    GpsMainCompose()
                }
            }
        }
    }

    private fun createWorkManager() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                val locationWorkService = PeriodicWorkRequestBuilder<GpsAlarmWorkManager>(1, TimeUnit.SECONDS)
                    .setInputData(workDataOf(
                        ADDRESS_LIST to viewModel.getAddressList()
                    ))
                    .build()
                WorkManager.getInstance(this@GpsAlarmActivity).enqueue(locationWorkService)
            }
        }
    }
}