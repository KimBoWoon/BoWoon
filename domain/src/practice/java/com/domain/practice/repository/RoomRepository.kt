package com.domain.practice.repository

import com.domain.practice.dto.PokemonModel
import kotlinx.coroutines.flow.Flow

interface RoomRepository {
    suspend fun insert(value: PokemonModel.Pokemon)
    suspend fun findPokemon(name: String): PokemonModel.Pokemon?
    suspend fun delete(pokemon: PokemonModel.Pokemon): Int
    suspend fun deleteAll(): Int
    suspend fun getAllWishPokemon(limit: Int): Flow<List<PokemonModel.Pokemon>>
}