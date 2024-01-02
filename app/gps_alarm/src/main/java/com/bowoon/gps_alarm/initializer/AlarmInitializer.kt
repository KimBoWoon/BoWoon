package com.bowoon.gps_alarm.initializer

import android.content.Context
import androidx.startup.Initializer
import com.bowoon.gps_alarm.ui.util.AlarmManager
import javax.inject.Inject

class AlarmInitializer : Initializer<AlarmInitializer.Works> {
    @Inject
    lateinit var manager: AlarmManager

    override fun create(context: Context): Works {
        AlarmInitializerEntryPoint.resolve(context).inject(this)
        return Works(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()

    class Works(context: Context)
}