package com.gps_alarm

import android.app.Application
import com.bowoon.android.gps_alarm.BuildConfig
import com.naver.maps.map.NaverMapSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GpsAlarmApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        NaverMapSdk.getInstance(this).apply {
            client = NaverMapSdk.NaverCloudPlatformClient(BuildConfig.NAVER_MAPS_CLIENT_KEY)
        }
    }
}