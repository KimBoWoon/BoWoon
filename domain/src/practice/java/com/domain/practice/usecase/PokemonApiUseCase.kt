package com.domain.practice.usecase

import com.domain.practice.dto.PokemonData
import com.domain.practice.repository.PokemonApiRepository

class PokemonApiUseCase(
    private val pokemonApiRepository: PokemonApiRepository
) {
    suspend fun getPokemon(limit: Int, offset: Int): PokemonData =
        pokemonApiRepository.getPokemon(limit, offset)
}