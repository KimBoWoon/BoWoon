package com.data.practice.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.domain.practice.dto.PokemonModel

@Dao
interface PokemonDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pokemon: Pokemon)

//    @Query("SELECT * FROM wish_pokemon")
//    fun getAllWishPokemon(): PagingSource<Int, WishPokemon>
//
//    @Delete
//    fun delete(pokemon: WishPokemon)
//
//    @Query("SELECT * FROM pokemon")
//    fun getPokemonList(): PagingSource<Int, PokemonModel.Pokemon>
//
//    @Query("DELETE FROM pokemon")
//    fun deleteAll()
//
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