package com.bowoon.gps_alarm.webview

import android.os.Bundle
import android.webkit.WebSettings
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.bowoon.commonutils.Log
import com.bowoon.gpsAlarm.R
import com.bowoon.gpsAlarm.databinding.AddressWebViewActivityBinding
import com.bowoon.gps_alarm.ui.alarm.CreateAlarmFragment
import com.bowoon.gps_alarm.ui.viewmodel.AlarmVM
import com.bowoon.gps_alarm.webview.javascript.GpsAlarmInterface
import com.bowoon.gps_alarm.webview.javascript.GpsAlarmInterfaceImpl
import com.bowoon.gps_alarm.webview.settings.WebViewChromeClient
import com.bowoon.gps_alarm.webview.settings.WebViewClient
import com.bowoon.commonutils.DataStatus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddressWebViewActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "#AddressWebViewActivity"
    }

    private val binding: AddressWebViewActivityBinding by lazy {
        DataBindingUtil.setContentView(
            this@AddressWebViewActivity,
            R.layout.address_web_view_activity
        )
    }
    private val viewModel by viewModels<AlarmVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.webview.apply {
            webViewClient = WebViewClient(binding)
            webChromeClient = WebViewChromeClient()

            settings.apply {
                javaScriptEnabled = true
                javaScriptCanOpenWindowsAutomatically = true
                domStorageEnabled = true
                useWideViewPort = true
                loadWithOverviewMode = true
                builtInZoomControls = false
                cacheMode = WebSettings.LOAD_NO_CACHE
                allowContentAccess = true
                allowFileAccess = true
                setSupportZoom(false)
                setSupportMultipleWindows(true)
            }

            val executeCallback: (String) -> Unit = {
                viewModel.getGeocode(it)
            }

            addJavascriptInterface(GpsAlarmInterfaceImpl(executeCallback), GpsAlarmInterface.INTERFACE_NAME)

            loadUrl("http:///172.30.50.183/address.html")
        }

        initFlow()
    }

    private fun initFlow() {
        lifecycleScope.launch {
            viewModel.geocode.collectLatest {
                when (it) {
                    is DataStatus.Loading -> {
                        Log.d(TAG, "geocode loading...")
                    }
                    is DataStatus.Success -> {
                        setResult(
                            CreateAlarmFragment.ADDRESS_DATA_RESULT_CODE,
                            intent.apply {
                                putExtra(CreateAlarmFragment.RESULT_ADDRESS, it.data)
                            }
                        )
                        finish()
                    }
                    is DataStatus.Failure -> {
                        Log.printStackTrace(it.throwable)
                    }
                }
            }
        }
    }
}