package com.bowoon.component.apis

import com.bowoon.component.data.PokemonData
import retrofit2.http.GET
import retrofit2.http.Query

interface PokemonApiService {
    /**
     * 200
     */
    @GET("https://pokeapi.co/api/v2/pokemon")
    suspend fun getAllPokemon(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): PokemonData
}