package com.domain.practice.repository

import com.domain.practice.dto.Pokemon
import kotlinx.coroutines.flow.Flow

interface RoomRepository {
    suspend fun insert(value: Pokemon)
    suspend fun findPokemon(name: String): Pokemon?
    suspend fun delete(pokemon: Pokemon): Int
    suspend fun deleteAll(): Int
    suspend fun getAllWishPokemon(limit: Int): Flow<List<Pokemon>>
}