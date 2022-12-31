package com.gps_alarm.base

import android.os.Bundle
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.annotation.CallSuper
import com.bowoon.android.gps_alarm.BuildConfig

abstract class BaseActivity : ComponentActivity() {
    companion object {
        private const val TAG = "BaseActivity"
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG)
    }
}