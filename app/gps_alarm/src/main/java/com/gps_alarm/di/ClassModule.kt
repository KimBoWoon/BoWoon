package com.gps_alarm.di

import android.content.Context
import com.data.gpsAlarm.local.LocalDatastore
import com.gps_alarm.paging.room.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object ClassModule {
    @Provides
    fun provideLocalDataStore(
        @ApplicationContext context: Context
    ): LocalDatastore = LocalDatastore(context)

    @Provides
    fun provideRoomDataBase(
        @ApplicationContext context: Context
    ): AppDatabase = AppDatabase.getInstance(context)
}