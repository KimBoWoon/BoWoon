package com.domain.practice.repository

import com.domain.practice.dto.SealedPokemon
import kotlinx.coroutines.flow.Flow

interface RoomRepository {
    suspend fun insert(value: SealedPokemon.Pokemon)
    suspend fun findPokemon(name: String): SealedPokemon.Pokemon?
    suspend fun delete(pokemon: SealedPokemon.Pokemon): Int
    suspend fun deleteAll(): Int
    suspend fun getAllWishPokemon(limit: Int): Flow<List<SealedPokemon.Pokemon>>
}