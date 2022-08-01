package com.lol.di

import com.domain.lol.repository.DataDragonApiRepository
import com.domain.lol.repository.RiotApiRepository
import com.domain.lol.usecase.DataDragonApiUseCase
import com.domain.lol.usecase.RiotApiUseCase
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
}