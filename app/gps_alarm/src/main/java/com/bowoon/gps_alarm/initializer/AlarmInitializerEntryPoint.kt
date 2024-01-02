package com.bowoon.gps_alarm.initializer

import android.content.Context
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AlarmInitializerEntryPoint {
    companion object {
        fun resolve(context: Context): AlarmInitializerEntryPoint {
            val appContext = context.applicationContext ?: throw IllegalStateException()
            return EntryPointAccessors.fromApplication(
                appContext,
                AlarmInitializerEntryPoint::class.java
            )
        }
    }

    fun inject(initializer: AlarmInitializer)
}