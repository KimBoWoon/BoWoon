package com.data.practice.mapper

import com.data.practice.dto.PokemonResponse
import com.data.practice.dto.RoomPokemon
import com.domain.practice.dto.Pokemon
import com.domain.practice.dto.PokemonData

fun PokemonResponse.convertPokemonList(): PokemonData =
    PokemonData(count, next, previous, results?.map { Pokemon(it.name, it.url) })

fun RoomPokemon.convertPokemon(): Pokemon = Pokemon(name, url)