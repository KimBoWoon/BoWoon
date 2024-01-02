package com.bowoon.gps_alarm.apis

import com.bowoon.network.JsonConverterRetrofit
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Apis @Inject constructor(
    @JsonConverterRetrofit private val retrofit: Retrofit
) {
    val mapsApi = retrofit.create(MapsApiService::class.java)
}