package com.bowoon.practice.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bowoon.practice.data.RoomPokemon
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pokemon: RoomPokemon)

    @Query("SELECT * FROM pokemon LIMIT :limit")
    fun getAllWishPokemon(limit: Int): Flow<List<RoomPokemon>>

    @Delete
    suspend fun delete(pokemon: RoomPokemon): Int

    @Query("DELETE FROM pokemon")
    suspend fun deleteAll(): Int

    @Query("SELECT * FROM pokemon WHERE pokemon.name = :name")
    suspend fun findPokemon(name: String): RoomPokemon?
}