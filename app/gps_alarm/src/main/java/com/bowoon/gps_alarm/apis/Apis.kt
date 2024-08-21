package com.bowoon.gps_alarm.apis

import com.bowoon.network.JsonConverterRetrofit
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Apis @Inject constructor(
    @JsonConverterRetrofit private val retrofit: Retrofit,
    private val httpClient: OkHttpClient,
    private val interceptor: AppInterceptor
) {
    val mapsApi = retrofit.newBuilder()
        .client(
            httpClient.newBuilder()
                .addInterceptor(interceptor)
                .build()
        )
        .build().create(MapsApiService::class.java)
}