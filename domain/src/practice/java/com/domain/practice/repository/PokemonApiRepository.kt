package com.domain.practice.repository

import com.domain.practice.dto.PokemonData

interface PokemonApiRepository {
    suspend fun getPokemon(limit: Int, offset: Int): PokemonData
}