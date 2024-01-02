package com.bowoon.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
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
object RetrofitModule {
    @JsonConverterRetrofit
    @Provides
    fun provideJsonRetrofit(
        client: OkHttpClient,
        serialization: Json,
        jsonMediaType: MediaType
    ): Retrofit = Retrofit.Builder()
        .baseUrl("https://localhost.com")
        .addConverterFactory(serialization.asConverterFactory(jsonMediaType))
        .client(client)
        .build()

    @XmlConverterRetrofit
    @Provides
    fun provideXmlRetrofit(
        client: OkHttpClient,
        tikXmlFactory: TikXmlConverterFactory
    ): Retrofit = Retrofit.Builder()
        .baseUrl("https://localhost.com")
        .addConverterFactory(tikXmlFactory)
        .client(client)
        .build()
}