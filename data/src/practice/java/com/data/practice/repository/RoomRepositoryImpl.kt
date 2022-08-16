package com.data.practice.repository

import com.data.practice.dto.Pokemon
import com.data.practice.room.RoomDataBase
import com.domain.practice.dto.PokemonModel
import com.domain.practice.repository.RoomRepository
import kotlinx.coroutines.flow.Flow

class RoomRepositoryImpl(
    private val roomDataBase: RoomDataBase
) : RoomRepository {
    override suspend fun insert(value: PokemonModel.Pokemon) {
        roomDataBase.roomPokemonDao().insert(Pokemon(name = value.name ?: "", url = value.url ?: ""))
    }

    override suspend fun findPokemon(name: String): PokemonModel.Pokemon? {
        roomDataBase.roomPokemonDao().findPokemon(name)?.let { pokemon ->
            return PokemonModel.Pokemon(pokemon.name, pokemon.url)
        } ?: run {
            return null
        }
    }

    override suspend fun delete(pokemon: PokemonModel.Pokemon): Int {
        roomDataBase.roomPokemonDao().findPokemon(pokemon.name ?: "")?.let {
            return roomDataBase.roomPokemonDao().delete(it)
        } ?: run {
            return 0
        }
    }

    override suspend fun deleteAll(): Int =
        roomDataBase.roomPokemonDao().deleteAll()

    override suspend fun getAllWishPokemon(limit: Int): Flow<List<PokemonModel.Pokemon>> =
        roomDataBase.roomPokemonDao().getAllWishPokemon(limit)
}