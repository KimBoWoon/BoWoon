package com.gps_alarm

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.work.Configuration
import com.gps_alarm.ui.util.CustomApplicationLifecycle
import com.gps_alarm.ui.util.createNotificationChannel
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class GpsAlarmApplication : Application(), Configuration.Provider {
    private val applicationLifecycle = CustomApplicationLifecycle()
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

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