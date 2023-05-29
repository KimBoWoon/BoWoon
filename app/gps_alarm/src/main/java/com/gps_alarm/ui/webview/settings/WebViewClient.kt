package com.gps_alarm.ui.webview.settings

import android.graphics.Bitmap
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import com.google.accompanist.web.AccompanistWebViewClient
import util.Log

class WebViewClient : AccompanistWebViewClient() {
    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        Log.d("compose webview loading...")
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        Log.d("compose webview finish!")
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