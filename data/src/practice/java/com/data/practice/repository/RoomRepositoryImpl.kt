package com.data.practice.repository

import com.data.practice.room.Pokemon
import com.data.practice.room.RoomHelper
import com.domain.practice.dto.PokemonModel
import com.domain.practice.repository.RoomRepository
import kotlinx.coroutines.flow.Flow

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

    override suspend fun deleteAll(): Int =
        roomHelper.roomPokemonDao().deleteAll()

    override suspend fun getAllWishPokemon(limit: Int): Flow<List<PokemonModel.Pokemon>> =
        roomHelper.roomPokemonDao().getAllWishPokemon(limit)
}