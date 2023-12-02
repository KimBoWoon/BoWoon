package com.bowoon.gps_alarm.javascript

import android.webkit.JavascriptInterface
import com.bowoon.gps_alarm.javascript.GpsAlarmInterface
import com.data.util.Log

class GpsAlarmInterfaceImpl(
    private val callback: ((String) -> Unit)? = null
) : GpsAlarmInterface {
    @JavascriptInterface
    override fun onComplete(data: String) {
        Log.d("choose address >>>>> $data")
        callback?.invoke(data)
    }
}