package com.rss_reader.activities

import android.os.Bundle
import android.webkit.WebSettings
import com.rss_reader.R
import com.rss_reader.base.BaseActivity
import com.rss_reader.databinding.ActivityArticleDetailBinding
import com.rss_reader.setting.RssWebChromeClient
import com.rss_reader.setting.RssWebViewClient
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleDetailActivity : BaseActivity<ActivityArticleDetailBinding>(R.layout.activity_article_detail) {
    private var loadUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            lifecycleOwner = this@ArticleDetailActivity
        }

        loadUrl = intent.getStringExtra("loadUrl") ?: ""

        initBinding()
    }

    private fun initBinding() {
        binding.apply {
            wvArticleDetail.apply {
                webViewClient = RssWebViewClient(binding)
                webChromeClient = RssWebChromeClient()
                settings.apply {
                    setSupportZoom(false)
                    builtInZoomControls = false
                    cacheMode = WebSettings.LOAD_NO_CACHE
                    mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                    defaultTextEncodingName = "UTF-8"
                }

                loadUrl(loadUrl)
            }
        }
    }
}