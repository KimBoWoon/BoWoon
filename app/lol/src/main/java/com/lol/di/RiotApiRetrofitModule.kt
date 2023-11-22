package com.lol.di

import com.data.lol.service.RiotApiService
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
object RiotApiRetrofitModule {
    @Provides
    fun provideRiotRetrofit(
        client: OkHttpClient,
        serialization: Json,
        jsonMediaType: MediaType
    ): RiotApiService = Retrofit.Builder()
        .baseUrl("https://kr.api.riotgames.com/")
        .addConverterFactory(serialization.asConverterFactory(jsonMediaType))
        .client(client)
        .build()
        .create(RiotApiService::class.java)

//    @Provides
//    fun provideRiotApiBaseUrl(): String = "https://kr.api.riotgames.com/"
}