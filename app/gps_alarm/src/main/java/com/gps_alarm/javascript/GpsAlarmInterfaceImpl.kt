package com.gps_alarm.javascript

import android.webkit.JavascriptInterface
import util.Log

class GpsAlarmInterfaceImpl(
    private val callback: (String) -> Unit
) : GpsAlarmInterface {
    @JavascriptInterface
    override fun onComplete(data: String) {
        Log.d("choose address >>>>> $data")
        callback.invoke(data)
    }
}