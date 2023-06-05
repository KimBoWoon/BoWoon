package com.gps_alarm.ui.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.Surface
import com.gps_alarm.base.BaseActivity
import com.gps_alarm.ui.main.GpsMainCompose
import com.gps_alarm.ui.theme.GpsAlarmTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GpsAlarmActivity : BaseActivity() {
    private val TAG = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GpsAlarmTheme {
                Surface {
                    GpsMainCompose()
                }
            }
        }
    }
}