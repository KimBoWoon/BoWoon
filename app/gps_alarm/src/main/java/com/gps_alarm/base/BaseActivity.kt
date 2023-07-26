package com.gps_alarm.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.annotation.CallSuper

abstract class BaseActivity : ComponentActivity() {
    companion object {
        private const val TAG = "BaseActivity"
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}