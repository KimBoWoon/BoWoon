package com.bowoon.lol.apis

import com.bowoon.network.JsonConverterRetrofit
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Apis @Inject constructor(
    @JsonConverterRetrofit private val retrofit: Retrofit
) {
    val dataDragonApi = retrofit.create(DataDragonApiService::class.java)
    val riotApi = retrofit.create(RiotApiService::class.java)
}