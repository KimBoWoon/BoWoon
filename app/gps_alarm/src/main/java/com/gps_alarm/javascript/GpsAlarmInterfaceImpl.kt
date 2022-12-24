package com.gps_alarm.javascript

import android.webkit.JavascriptInterface
import androidx.databinding.ViewDataBinding
import com.gps_alarm.base.BaseDialog
import com.gps_alarm.ui.dialog.ShowAddressDialog
import util.Log

class GpsAlarmInterfaceImpl(
    val dialog: BaseDialog<out ViewDataBinding>? = null
) : GpsAlarmInterface {
    @JavascriptInterface
    override fun onComplete(data: String) {
        Log.d("choose address >>>>> $data")
        (dialog as? ShowAddressDialog)?.invokeAddressCallback(data)
        (dialog as? ShowAddressDialog)?.dismissAllowingStateLoss()
    }
}