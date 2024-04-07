package com.bowoon.component.setting

import android.graphics.Bitmap
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.ViewDataBinding
import com.bowoon.commonutils.Log

class WebViewClient(
    private val binding: ViewDataBinding? = null
) : WebViewClient() {
    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)

        Log.d("onPageStarted")
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)

        Log.d("onPageFinished")
    }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)

        Log.e("ArticleDetailWebViewError > url : ${request?.url}, code : ${error?.errorCode}, message : ${error?.description}")
    }
}