package com.gps_alarm.di

import android.content.Context
import com.data.gpsAlarm.local.LocalDatastore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json

@Module
@InstallIn(ViewModelComponent::class)
object ClassModule {
    @Provides
    fun provideLocalDataStore(
        @ApplicationContext context: Context
    ): LocalDatastore = LocalDatastore(context)

//    @Provides
//    fun provideJson(): Json = Json { ignoreUnknownKeys = true }
}