package com.data.practice.room

import androidx.room.*
import com.data.practice.dto.Pokemon
import com.domain.practice.dto.PokemonModel
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pokemon: Pokemon)

    @Query("SELECT * FROM pokemon LIMIT :limit")
    fun getAllWishPokemon(limit: Int): Flow<List<PokemonModel.Pokemon>>

    @Delete
    suspend fun delete(pokemon: Pokemon): Int

    @Query("DELETE FROM pokemon")
    suspend fun deleteAll(): Int

    @Query("SELECT * FROM pokemon WHERE pokemon.name = :name")
    suspend fun findPokemon(name: String): Pokemon?
}