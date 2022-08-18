package com.data.practice.mapper

import com.data.practice.dto.RoomPokemon
import com.data.practice.dto.PokemonResponse
import com.domain.practice.dto.PokemonData
import com.domain.practice.dto.SealedPokemon

fun PokemonResponse.convertPokemonList(): PokemonData {
    return PokemonData(count, next, previous, results?.map { SealedPokemon.Pokemon(it.name, it.url) })
}

fun RoomPokemon.convertPokemon(): SealedPokemon.Pokemon {
    return SealedPokemon.Pokemon(name, url)
}