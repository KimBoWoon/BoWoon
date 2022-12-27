package com.gps_alarm.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.annotation.CallSuper

abstract class BaseActivity : ComponentActivity() {
    companion object {
        private const val TAG = "BaseActivity"
        const val PERMISSION_REQUEST_CODE = 1000
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}