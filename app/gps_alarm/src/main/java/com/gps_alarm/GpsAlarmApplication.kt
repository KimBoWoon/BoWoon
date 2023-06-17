package com.gps_alarm

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import com.gps_alarm.ui.util.CustomApplicationLifecycle
import com.gps_alarm.ui.util.createNotificationChannel
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GpsAlarmApplication : Application() {
    private val applicationLifecycle = CustomApplicationLifecycle()

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel(applicationContext)

        ProcessLifecycleOwner.get().lifecycle.addObserver(applicationLifecycle)
    }

    override fun onTerminate() {
        super.onTerminate()

        ProcessLifecycleOwner.get().lifecycle.removeObserver(applicationLifecycle)
    }
}