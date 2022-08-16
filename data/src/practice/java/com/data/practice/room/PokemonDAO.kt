package com.data.practice.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.domain.practice.dto.PokemonModel
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pokemon: Pokemon)

    @Query("SELECT * FROM pokemon LIMIT :limit")
    fun getAllWishPokemon(limit: Int): Flow<List<PokemonModel.Pokemon>>

//    @Delete
//    fun delete(pokemon: WishPokemon)
//
//    @Query("SELECT * FROM pokemon")
//    fun getPokemonList(): PagingSource<Int, PokemonModel.Pokemon>

    @Query("DELETE FROM pokemon")
    suspend fun deleteAll(): Int

//    @Query("DELETE FROM wish_pokemon")
//    fun wishDeleteAll()
//
    @Query("SELECT * FROM pokemon WHERE pokemon.name = :name")
    suspend fun findPokemon(name: String): Pokemon?
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertAll(results: List<RoomPokemon>)
//
//    @Query("SELECT * FROM wish_pokemon")
//    fun getWishPokemon(): PagingSource<Int, WishPokemon>
//
//    @Query("SELECT * FROM wish_pokemon")
//    fun getWishPokemonList(): List<WishPokemon>?
}