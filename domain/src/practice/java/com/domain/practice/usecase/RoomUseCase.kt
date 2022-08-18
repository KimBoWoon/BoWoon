package com.domain.practice.usecase

import com.domain.practice.dto.SealedPokemon
import com.domain.practice.repository.RoomRepository
import kotlinx.coroutines.flow.Flow

class RoomUseCase(
    private val roomRepository: RoomRepository
) {
    suspend fun insert(value: SealedPokemon.Pokemon) =
        roomRepository.insert(value)

    suspend fun findPokemon(name: String): SealedPokemon.Pokemon? =
        roomRepository.findPokemon(name)

    suspend fun delete(pokemon: SealedPokemon.Pokemon): Int =
        roomRepository.delete(pokemon)

    suspend fun deleteAll(): Int =
        roomRepository.deleteAll()

    suspend fun getAllWishPokemon(limit: Int): Flow<List<SealedPokemon.Pokemon>> =
        roomRepository.getAllWishPokemon(limit)
}