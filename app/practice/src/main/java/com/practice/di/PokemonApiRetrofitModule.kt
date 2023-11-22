package com.practice.di

import com.data.practice.service.PokemonApiService
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
object PokemonApiRetrofitModule {
    @Provides
    fun providePokemonRetrofit(
        baseUrl: String,
        client: OkHttpClient,
        serialization: Json,
        jsonMediaType: MediaType
    ): PokemonApiService = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(serialization.asConverterFactory(jsonMediaType))
        .client(client)
        .build()
        .create(PokemonApiService::class.java)

//    @Provides
//    fun providePokemonApiBaseUrl(): String = "https://pokeapi.co/api/v2/"
}