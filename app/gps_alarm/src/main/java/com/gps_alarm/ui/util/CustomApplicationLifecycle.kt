package com.gps_alarm.ui.util

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import util.Log

class CustomApplicationLifecycle : DefaultLifecycleObserver {
    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        Log.d("CustomApplicationLifecycle_onCreate")
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)

        Log.d("CustomApplicationLifecycle_onStart")
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)

        Log.d("CustomApplicationLifecycle_onResume")
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)

        Log.d("CustomApplicationLifecycle_onPause")
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)

        Log.d("CustomApplicationLifecycle_onStop")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)

        Log.d("CustomApplicationLifecycle_onDestroy")
    }
}