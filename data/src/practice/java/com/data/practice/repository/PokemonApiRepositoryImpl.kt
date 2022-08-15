package com.data.practice.repository

import com.data.practice.service.PokemonApiService
import com.domain.practice.dto.PokemonResponse
import com.domain.practice.repository.PokemonApiRepository

class PokemonApiRepositoryImpl(
    private val pokemonApiService: PokemonApiService
) : PokemonApiRepository {
    override suspend fun getAllPokemon(limit: Int, offset: Int): PokemonResponse =
        pokemonApiService.getAllPokemon(limit, offset)
}