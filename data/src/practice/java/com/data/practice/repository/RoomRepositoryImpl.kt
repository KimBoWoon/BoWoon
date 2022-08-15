package com.data.practice.repository

import androidx.paging.PagingSource
import com.data.practice.room.Pokemon
import com.data.practice.room.RoomHelper
import com.data.practice.room.WishPokemon
import com.domain.practice.dto.PokemonModel
import com.domain.practice.repository.RoomRepository

class RoomRepositoryImpl(
    private val roomHelper: RoomHelper
) : RoomRepository {
    override suspend fun insert(value: PokemonModel.Pokemon) {
        roomHelper.roomPokemonDao().insert(Pokemon(name = value.name ?: "", url = value.url ?: ""))
    }

    override suspend fun findPokemon(name: String): PokemonModel.Pokemon? {
        roomHelper.roomPokemonDao().findPokemon(name)?.let { pokemon ->
            return PokemonModel.Pokemon(pokemon.name, pokemon.url)
        }
        return null
    }

    //    override suspend fun getAllWishPokemon(): PagingSource<Int, WishPokemon> =
//        roomHelper.roomPokemonDao().getAllWishPokemon()
}