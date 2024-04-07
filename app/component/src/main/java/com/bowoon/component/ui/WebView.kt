package com.bowoon.component.ui

import android.os.Bundle
import android.webkit.WebSettings
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bowoon.component.R
import com.bowoon.component.databinding.ActivityWebViewBinding
import com.bowoon.component.setting.WebChromeClient
import com.bowoon.component.setting.WebViewClient
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebView : AppCompatActivity() {
    private val binding: ActivityWebViewBinding by lazy {
        DataBindingUtil.setContentView(this@WebView, R.layout.activity_web_view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            lifecycleOwner = this@WebView
        }

        onBackPressedDispatcher.addCallback {
            if (binding.webview.canGoBack()) {
                binding.webview.goBack()
            } else {
                finish()
            }
        }

        initBinding()
    }

    private fun initBinding() {
        binding.webview.apply {
            webViewClient = WebViewClient(binding)
            webChromeClient = WebChromeClient()
            settings.apply {
                setSupportZoom(false)
                javaScriptEnabled = true
                builtInZoomControls = false
                cacheMode = WebSettings.LOAD_NO_CACHE
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                defaultTextEncodingName = "UTF-8"
            }
            loadUrl(intent.getStringExtra("url") ?: "")
        }
    }
}