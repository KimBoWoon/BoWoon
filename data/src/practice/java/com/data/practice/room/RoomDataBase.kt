package com.data.practice.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.data.practice.dto.Pokemon

@Database(entities = [Pokemon::class], version = 1, exportSchema = false)
abstract class RoomDataBase : RoomDatabase() {
    abstract fun roomPokemonDao(): PokemonDAO
}