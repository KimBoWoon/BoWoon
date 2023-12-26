package com.bowoon.gps_alarm.webview.settings

import android.graphics.Bitmap
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.isVisible
import com.bowoon.gpsAlarm.databinding.AddressWebViewActivityBinding
import com.data.util.Log

class WebViewClient(
    private val binding: AddressWebViewActivityBinding
) : WebViewClient() {
    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        Log.d("compose webview loading...")

        binding.pbLoading.isVisible = true
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        Log.d("compose webview finish!")

        binding.pbLoading.isVisible = false
    }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)
        Log.d("compose webview error! > ${error?.errorCode}, ${error?.description}")
    }
}