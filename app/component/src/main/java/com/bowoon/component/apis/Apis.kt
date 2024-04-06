package com.bowoon.component.apis

import com.bowoon.network.JsonConverterRetrofit
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Apis @Inject constructor(
    @JsonConverterRetrofit private val retrofit: Retrofit
) {
    val pokemonApi: PokemonApiService = retrofit.create(PokemonApiService::class.java)
}