package com.practice.di

import com.data.practice.local.LocalDatastore
import com.data.practice.repository.DataStoreRepositoryImpl
import com.data.practice.repository.PokemonApiRepositoryImpl
import com.data.practice.repository.RoomRepositoryImpl
import com.data.practice.room.RoomDataBase
import com.data.practice.service.PokemonApiService
import com.domain.practice.repository.DataStoreRepository
import com.domain.practice.repository.PokemonApiRepository
import com.domain.practice.repository.RoomRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {
    @Provides
    fun providePokemonApiRepository(
        pokemonApiService: PokemonApiService
    ): PokemonApiRepository = PokemonApiRepositoryImpl(pokemonApiService)

    @Provides
    fun provideRoomRepository(
        roomDataBase: RoomDataBase
    ): RoomRepository = RoomRepositoryImpl(roomDataBase)

    @Provides
    fun provideDatastore(
        dataStore: LocalDatastore
    ): DataStoreRepository = DataStoreRepositoryImpl(dataStore)
}