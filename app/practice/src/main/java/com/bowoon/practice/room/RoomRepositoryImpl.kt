package com.bowoon.practice.room

import com.bowoon.practice.data.Pokemon
import com.bowoon.practice.data.RoomPokemon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomRepository(
    private val roomDataBase: RoomDataBase
) {
    suspend fun insert(value: Pokemon) {
        roomDataBase.roomPokemonDao().insert(RoomPokemon(name = value.name ?: "", url = value.url ?: ""))
    }

    suspend fun findPokemon(name: String): Pokemon? {
        roomDataBase.roomPokemonDao().findPokemon(name)?.let { pokemon ->
            return Pokemon(name = pokemon.name, url = pokemon.url)
        } ?: run {
            return null
        }
    }

    suspend fun delete(pokemon: Pokemon): Int {
        roomDataBase.roomPokemonDao().findPokemon(pokemon.name ?: "")?.let {
            return roomDataBase.roomPokemonDao().delete(it)
        } ?: run {
            return 0
        }
    }

    suspend fun deleteAll(): Int =
        roomDataBase.roomPokemonDao().deleteAll()

    suspend fun getAllWishPokemon(limit: Int): Flow<List<Pokemon>> {
        return roomDataBase.roomPokemonDao().getAllWishPokemon(limit).map { pokemonList ->
            pokemonList.map { pokemon ->
                Pokemon(pokemon.name, pokemon.url)
            }
        }
    }
}