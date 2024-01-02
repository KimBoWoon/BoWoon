package com.bowoon.practice.initializer

import android.content.Context
import androidx.startup.Initializer

class AppInitializer : Initializer<AppInitializer.Works> {
    override fun create(context: Context): Works = Works(context)

    override fun dependencies(): List<Class<out Initializer<*>>> =
        emptyList()

    class Works(context: Context)
}