package com.gps_alarm.javascript

import android.webkit.JavascriptInterface
import androidx.databinding.ViewDataBinding
import com.gps_alarm.base.BaseDialog
import com.gps_alarm.ui.dialog.ShowAddressDialog
import util.Log

interface GpsAlarmInterface {
    companion object {
        const val INTERFACE_NAME: String = "gpsAlarm"
    }

    fun onComplete(data: String)
}