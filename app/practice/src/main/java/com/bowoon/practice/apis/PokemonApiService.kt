package com.bowoon.practice.apis

import com.bowoon.practice.data.PokemonDataConstant
import com.bowoon.practice.data.PokemonResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface PokemonApiService {
    /**
     * 200
     */
    @GET
    suspend fun getAllPokemon(
        @Url url: String,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): PokemonResponse

    /**
     * 404
     */
//    @GET("asdf")
//    suspend fun getPokemon(
//        @Query("limit") limit: Int = 20,
//        @Query("offset") offset: Int = 0
//    ): PokemonResponse
}