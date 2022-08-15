package com.data.practice.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Pokemon::class], version = 1, exportSchema = false)
abstract class RoomHelper : RoomDatabase() {
    abstract fun roomPokemonDao(): PokemonDAO

//    companion object {
//        private var INSTANCE: RoomHelper? = null
//
//        fun getInstance(context: Context): RoomHelper {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(context, RoomHelper::class.java, "room_pokemon")
//                    .fallbackToDestructiveMigration()
//                    .build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
}