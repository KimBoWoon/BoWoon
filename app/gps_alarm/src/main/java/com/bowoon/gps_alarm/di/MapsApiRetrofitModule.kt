package com.bowoon.gps_alarm.di

import com.data.gpsAlarm.service.MapsApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object MapsApiRetrofitModule {
    @Provides
    fun provideMapsRetrofit(
        baseUrl: String,
        client: OkHttpClient,
        serialization: Json,
        jsonMediaType: MediaType
    ): MapsApiService = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(serialization.asConverterFactory(jsonMediaType))
        .client(client)
        .build()
        .create(MapsApiService::class.java)

    @Provides
    fun provideMapsApiBaseUrl(): String = "https://naveropenapi.apigw.ntruss.com/"
}