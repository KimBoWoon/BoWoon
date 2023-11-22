package com.data.practice.repository

import com.data.practice.dto.RoomPokemon
import com.data.practice.mapper.convertPokemon
import com.data.practice.room.RoomDataBase
import com.domain.practice.dto.Pokemon
import com.domain.practice.repository.RoomRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomRepositoryImpl(
    private val roomDataBase: RoomDataBase
) : RoomRepository {
    override suspend fun insert(value: Pokemon) {
        roomDataBase.roomPokemonDao().insert(RoomPokemon(name = value.name ?: "", url = value.url ?: ""))
    }

    override suspend fun findPokemon(name: String): Pokemon? {
        roomDataBase.roomPokemonDao().findPokemon(name)?.let { pokemon ->
            return RoomPokemon(name = pokemon.name, url = pokemon.url).convertPokemon()
        } ?: run {
            return null
        }
    }

    override suspend fun delete(pokemon: Pokemon): Int {
        roomDataBase.roomPokemonDao().findPokemon(pokemon.name ?: "")?.let {
            return roomDataBase.roomPokemonDao().delete(it)
        } ?: run {
            return 0
        }
    }

    override suspend fun deleteAll(): Int =
        roomDataBase.roomPokemonDao().deleteAll()

    override suspend fun getAllWishPokemon(limit: Int): Flow<List<Pokemon>> {
        return roomDataBase.roomPokemonDao().getAllWishPokemon(limit).map { pokemonList ->
            pokemonList.map { pokemon ->
                Pokemon(pokemon.name, pokemon.url)
            }
        }
    }
}