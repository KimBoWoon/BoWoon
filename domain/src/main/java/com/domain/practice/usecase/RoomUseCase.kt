package com.domain.practice.usecase

import com.domain.practice.dto.Pokemon
import com.domain.practice.repository.RoomRepository
import kotlinx.coroutines.flow.Flow

class RoomUseCase(
    private val roomRepository: RoomRepository
) {
    suspend fun insert(value: Pokemon) =
        roomRepository.insert(value)

    suspend fun findPokemon(name: String): Pokemon? =
        roomRepository.findPokemon(name)

    suspend fun delete(pokemon: Pokemon): Int =
        roomRepository.delete(pokemon)

    suspend fun deleteAll(): Int =
        roomRepository.deleteAll()

    suspend fun getAllWishPokemon(limit: Int): Flow<List<Pokemon>> =
        roomRepository.getAllWishPokemon(limit)
}