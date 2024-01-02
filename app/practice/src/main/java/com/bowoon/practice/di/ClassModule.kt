package com.bowoon.practice.di

import android.content.Context
import androidx.room.Room
import com.bowoon.practice.room.PokemonDAO
import com.bowoon.practice.room.RoomDataBase
import com.bowoon.practice.room.RoomRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object ClassModule {
    @Provides
    fun provideRoomHelper(
        @ApplicationContext context: Context
    ): RoomDataBase = Room.databaseBuilder(context, RoomDataBase::class.java, "room_pokemon")
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    fun providePokemon(appDatabase: RoomDataBase): PokemonDAO = appDatabase.roomPokemonDao()

    @Provides
    fun provideRoomRepository(
        room: RoomDataBase
    ): RoomRepository = RoomRepository(room)
}