package com.bowoon.gps_alarm.initializer

import android.content.Context
import androidx.startup.Initializer

class AppInitializer : Initializer<AppInitializer.Works> {
    override fun create(context: Context): Works = Works(context)

    override fun dependencies(): List<Class<out Initializer<*>>> =
        listOf(AlarmInitializer::class.java)

    class Works(context: Context)
}