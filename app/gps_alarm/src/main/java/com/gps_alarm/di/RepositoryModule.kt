package com.gps_alarm.di

import com.data.gpsAlarm.local.LocalDatastore
import com.data.gpsAlarm.repository.DataStoreRepositoryImpl
import com.data.gpsAlarm.repository.MapsApiRepositoryImpl
import com.data.gpsAlarm.service.MapsApiService
import com.domain.gpsAlarm.repository.DataStoreRepository
import com.domain.gpsAlarm.repository.MapsApiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {
    @Provides
    fun provideMapsApiRepository(
        mapsApiService: MapsApiService
    ): MapsApiRepository = MapsApiRepositoryImpl(mapsApiService)

    @Provides
    fun provideDatastore(
        dataStore: LocalDatastore
    ): DataStoreRepository = DataStoreRepositoryImpl(dataStore)
}