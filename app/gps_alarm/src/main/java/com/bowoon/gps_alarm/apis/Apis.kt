package com.bowoon.gps_alarm.apis

import com.bowoon.network.JsonConverterRetrofit
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Apis @Inject constructor(
    @JsonConverterRetrofit private val retrofit: Retrofit,
    private val httpClient: OkHttpClient
) {
    val mapsApi = retrofit.newBuilder()
        .client(httpClient.newBuilder().addInterceptor(AppInterceptor()).build())
        .build().create(MapsApiService::class.java)
}