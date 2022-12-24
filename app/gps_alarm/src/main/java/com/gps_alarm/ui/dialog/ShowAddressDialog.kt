package com.gps_alarm.ui.dialog

import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.fragment.app.viewModels
import com.bowoon.android.gps_alarm.R
import com.bowoon.android.gps_alarm.databinding.DialogInputAddressBinding
import com.gps_alarm.base.BaseDialog
import com.gps_alarm.javascript.GpsAlarmInterface
import com.gps_alarm.javascript.GpsAlarmInterfaceImpl

class ShowAddressDialog(
    private val addressCallback: ((String) -> Unit)? = null
) : BaseDialog<DialogInputAddressBinding>(
    R.layout.dialog_input_address, true, false
) {
    private val viewModel by viewModels<ShowAddressVM>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = this@ShowAddressDialog
        }
        lifecycle.addObserver(viewModel)

        binding?.wvAddress?.addJavascriptInterface(GpsAlarmInterfaceImpl(this@ShowAddressDialog), GpsAlarmInterface.INTERFACE_NAME)

        initBinding()
        initFlow()
    }

    private fun initBinding() {
        binding?.apply {
            wvAddress.apply {
                webViewClient = object : WebViewClient() {}
                webChromeClient = object : WebChromeClient() {}

                settings.apply {
                    javaScriptEnabled = true
                    javaScriptCanOpenWindowsAutomatically = true
                    domStorageEnabled = true
                    useWideViewPort = true
                    loadWithOverviewMode = true
                    builtInZoomControls = false
                    cacheMode = WebSettings.LOAD_NO_CACHE
                    setSupportZoom(false)
                    setSupportMultipleWindows(true)
                }
                loadUrl("http://192.168.35.128/address.html")
            }
        }
    }

    private fun initFlow() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.wvAddress?.removeJavascriptInterface(GpsAlarmInterface.INTERFACE_NAME)
        binding = null
    }

    fun invokeAddressCallback(data: String) {
        addressCallback?.invoke(data)
    }
}