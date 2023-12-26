package com.bowoon.gps_alarm.webview.javascript

import android.webkit.JavascriptInterface

interface GpsAlarmInterface {
    companion object {
        const val INTERFACE_NAME: String = "gpsAlarm"
    }

    @JavascriptInterface
    fun onComplete(data: String)
}