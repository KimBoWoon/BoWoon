package com.gps_alarm.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Surface
import androidx.lifecycle.lifecycleScope
import com.gps_alarm.base.BaseActivity
import com.gps_alarm.service.GpsAlarmService
import com.gps_alarm.ui.main.GpsMainCompose
import com.gps_alarm.ui.theme.GpsAlarmTheme
import com.gps_alarm.ui.viewmodel.GpsAlarmVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class GpsAlarmActivity : BaseActivity() {
    companion object {
        const val ALARM_WORK_MANAGER = "ALARM_WORK_MANAGER"
    }

    private val TAG = this.javaClass.simpleName
    private val viewModel by viewModels<GpsAlarmVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        createWorkManager()

        lifecycleScope.launch {
            val intent = Intent(this@GpsAlarmActivity, GpsAlarmService::class.java).apply {
                action = "StartService"
                putExtra("addressList", withContext(Dispatchers.IO) { viewModel.getAddressList() })
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                this@GpsAlarmActivity.startForegroundService(intent)
            } else {
                this@GpsAlarmActivity.startService(intent)
            }
        }

        setContent {
            GpsAlarmTheme {
                Surface {
                    GpsMainCompose()
                }
            }
        }
    }

//    private fun createWorkManager() {
//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                PeriodicWorkRequestBuilder<GpsAlarmWorkManager>(1, TimeUnit.SECONDS)
//                    .addTag(ALARM_WORK_MANAGER)
//                    .setInputData(
//                        workDataOf(
//                            ADDRESS_LIST to viewModel.getAddressList()
//                        )
//                    )
//                    .build().run {
//                        WorkManager.getInstance(this@GpsAlarmActivity).enqueue(this)
//                    }
//            }
//        }
//    }
}