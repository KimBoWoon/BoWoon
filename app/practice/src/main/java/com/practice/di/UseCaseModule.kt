package com.practice.di

import com.domain.practice.repository.DataStoreRepository
import com.domain.practice.repository.PokemonApiRepository
import com.domain.practice.repository.RoomRepository
import com.domain.practice.usecase.DataStoreUseCase
import com.domain.practice.usecase.PokemonApiUseCase
import com.domain.practice.usecase.RoomUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    @Provides
    fun providePokemonApiUseCase(
        dataDragonApiRepository: PokemonApiRepository
    ): PokemonApiUseCase = PokemonApiUseCase(dataDragonApiRepository)

    @Provides
    fun provideRoomUseCase(
        roomRepository: RoomRepository
    ): RoomUseCase = RoomUseCase(roomRepository)

    @Provides
    fun provideDataStoreUseCase(
        dataStoreRepository: DataStoreRepository
    ): DataStoreUseCase = DataStoreUseCase(dataStoreRepository)
}