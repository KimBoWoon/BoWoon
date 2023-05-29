package com.gps_alarm.javascript

import android.webkit.JavascriptInterface

interface GpsAlarmInterface {
    companion object {
        const val INTERFACE_NAME: String = "gpsAlarm"
    }

    @JavascriptInterface
    fun onComplete(data: String)
}