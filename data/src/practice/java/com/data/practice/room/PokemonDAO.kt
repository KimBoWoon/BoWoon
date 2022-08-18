package com.data.practice.room

import androidx.room.*
import com.data.practice.dto.RoomPokemon
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