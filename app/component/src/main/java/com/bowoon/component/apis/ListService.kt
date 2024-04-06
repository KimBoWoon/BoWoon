package com.bowoon.component.apis

import com.bowoon.component.data.PokemonData
import retrofit2.http.GET
import retrofit2.http.Url

interface ListService {
    @GET
    suspend fun getListData(
        @Url url: String,
    ): PokemonData
}