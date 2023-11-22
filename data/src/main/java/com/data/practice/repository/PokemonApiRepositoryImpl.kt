package com.data.practice.repository

import com.data.practice.mapper.convertPokemonList
import com.data.practice.service.PokemonApiService
import com.domain.practice.dto.PokemonData
import com.domain.practice.repository.PokemonApiRepository

class PokemonApiRepositoryImpl(
    private val pokemonApiService: PokemonApiService
) : PokemonApiRepository {
    override suspend fun getPokemon(limit: Int, offset: Int): PokemonData =
        pokemonApiService.getAllPokemon(limit, offset).convertPokemonList()
}