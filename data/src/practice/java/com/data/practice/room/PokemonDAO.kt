package com.data.practice.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.data.practice.dto.Pokemon
import com.domain.practice.dto.PokemonModel
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pokemon: Pokemon)

    @Query("SELECT * FROM pokemon LIMIT :limit")
    fun getAllWishPokemon(limit: Int): Flow<List<PokemonModel.Pokemon>>

    @Query("DELETE FROM pokemon")
    suspend fun deleteAll(): Int

    @Query("SELECT * FROM pokemon WHERE pokemon.name = :name")
    suspend fun findPokemon(name: String): Pokemon?
}