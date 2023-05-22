package com.rss_reader

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import dagger.hilt.android.HiltAndroidApp
import util.Log

@HiltAndroidApp
class RssReaderApplication : Application() {
    private val applicationLifecycle = CustomApplicationLifecycle()

    override fun onCreate() {
        super.onCreate()

        ProcessLifecycleOwner.get().lifecycle.addObserver(applicationLifecycle)
    }

    override fun onTerminate() {
        super.onTerminate()

        ProcessLifecycleOwner.get().lifecycle.removeObserver(applicationLifecycle)
    }
}

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