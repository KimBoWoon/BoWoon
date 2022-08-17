package com.domain.practice.usecase

import com.domain.practice.dto.PokemonResponse
import com.domain.practice.repository.PokemonApiRepository

class PokemonApiUseCase(
    private val pokemonApiRepository: PokemonApiRepository
) {
    suspend fun getPokemon(limit: Int, offset: Int): PokemonResponse =
        pokemonApiRepository.getPokemon(limit, offset)
}