package com.gps_alarm

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.bowoon.android.gps_alarm.R
import com.gps_alarm.ui.util.createNotificationChannel
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GpsAlarmApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        createNotificationChannel(applicationContext)
    }
}