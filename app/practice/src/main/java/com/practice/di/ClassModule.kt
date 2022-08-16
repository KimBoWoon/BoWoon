package com.practice.di

import android.content.Context
import androidx.room.Room
import com.data.practice.local.LocalDatastore
import com.data.practice.room.PokemonDAO
import com.data.practice.room.RoomDataBase
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
    fun provideRoomHelper(
        @ApplicationContext context: Context
    ): RoomDataBase = Room.databaseBuilder(context, RoomDataBase::class.java, "room_pokemon")
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    fun providePokemon(appDatabase: RoomDataBase): PokemonDAO = appDatabase.roomPokemonDao()
}