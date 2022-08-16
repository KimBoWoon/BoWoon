package com.domain.practice.usecase

import com.domain.practice.dto.PokemonModel
import com.domain.practice.repository.RoomRepository
import kotlinx.coroutines.flow.Flow

class RoomUseCase(
    private val roomRepository: RoomRepository
) {
    suspend fun insert(value: PokemonModel.Pokemon) =
        roomRepository.insert(value)

    suspend fun findPokemon(name: String): PokemonModel.Pokemon? =
        roomRepository.findPokemon(name)

    suspend fun delete(pokemon: PokemonModel.Pokemon): Int =
        roomRepository.delete(pokemon)

    suspend fun deleteAll(): Int =
        roomRepository.deleteAll()

    suspend fun getAllWishPokemon(limit: Int): Flow<List<PokemonModel.Pokemon>> =
        roomRepository.getAllWishPokemon(limit)
}