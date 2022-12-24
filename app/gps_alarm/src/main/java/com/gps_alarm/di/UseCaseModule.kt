package com.gps_alarm.di

import com.domain.gpsAlarm.repository.DataStoreRepository
import com.domain.gpsAlarm.repository.MapsApiRepository
import com.domain.gpsAlarm.usecase.DataStoreUseCase
import com.domain.gpsAlarm.usecase.MapsApiUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    @Provides
    fun provideMapsApiUseCase(
        mapsApiRepository: MapsApiRepository
    ): MapsApiUseCase = MapsApiUseCase(mapsApiRepository)

    @Provides
    fun provideDataStoreUseCase(
        dataStoreRepository: DataStoreRepository
    ): DataStoreUseCase = DataStoreUseCase(dataStoreRepository)
}