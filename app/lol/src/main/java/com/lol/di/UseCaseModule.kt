package com.lol.di

import com.domain.gpsAlarm.usecase.DataDragonApiUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    @Provides
    fun provideRiotApiUseCase(
        riotApiRepository: RiotApiRepository
    ): RiotApiUseCase = RiotApiUseCase(riotApiRepository)

    @Provides
    fun provideDataDragonApiUseCase(
        dataDragonApiRepository: DataDragonApiRepository
    ): DataDragonApiUseCase = DataDragonApiUseCase(dataDragonApiRepository)

    @Provides
    fun provideDataStoreUseCase(
        dataStoreRepository: DataStoreRepository
    ): DataStoreUseCase = DataStoreUseCase(dataStoreRepository)
}