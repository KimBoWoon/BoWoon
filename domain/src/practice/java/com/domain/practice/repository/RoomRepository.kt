package com.domain.practice.repository

import com.domain.practice.dto.PokemonModel
import kotlinx.coroutines.flow.Flow

interface RoomRepository {
    suspend fun insert(value: PokemonModel.Pokemon)
    suspend fun findPokemon(name: String): PokemonModel.Pokemon?
//    suspend fun getAllWishPokemon(): PagingSource<Int, WishPokemon>
}