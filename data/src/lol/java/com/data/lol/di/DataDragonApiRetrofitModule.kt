package com.data.lol.di

import com.data.lol.service.DataDragonApiService
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
object DataDragonApiRetrofitModule {
    @Provides
    fun provideDataDragonRetrofit(
        client: OkHttpClient,
        serialization: Json,
        jsonMediaType: MediaType
    ): DataDragonApiService = Retrofit.Builder()
        .baseUrl("https://ddragon.leagueoflegends.com/")
        .addConverterFactory(serialization.asConverterFactory(jsonMediaType))
        .client(client)
        .build()
        .create(DataDragonApiService::class.java)

//    @Provides
//    fun provideDataDragonApiBaseUrl(): String = "https://ddragon.leagueoflegends.com/"
}