package com.data.practice.service

import com.data.practice.dto.PokemonResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PokemonApiService {
    /**
     * 200
     */
    @GET("pokemon")
    suspend fun getAllPokemon(
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