package com.bowoon.practice.apis

import com.bowoon.network.JsonConverterRetrofit
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Apis @Inject constructor(
    @JsonConverterRetrofit private val retrofit: Retrofit
) {
    val pokemonApi = retrofit.create(PokemonApiService::class.java)
}