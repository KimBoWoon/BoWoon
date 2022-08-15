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

//    suspend fun getAllWishPokemon(): Flow<PokemonModel.Pokemon> = roomRepository.getAllWishPokemon()
}