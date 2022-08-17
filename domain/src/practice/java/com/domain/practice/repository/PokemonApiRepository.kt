package com.domain.practice.repository

import com.domain.practice.dto.PokemonResponse

interface PokemonApiRepository {
    suspend fun getPokemon(limit: Int, offset: Int): PokemonResponse
}