package com.practice.di

import android.content.Context
import androidx.room.Room
import com.data.practice.local.LocalDatastore
import com.data.practice.room.PokemonDAO
import com.data.practice.room.RoomHelper
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
    ): RoomHelper = Room.databaseBuilder(context, RoomHelper::class.java, "room_pokemon")
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    fun providePokemon(appDatabase: RoomHelper): PokemonDAO = appDatabase.roomPokemonDao()
}