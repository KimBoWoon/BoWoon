package com.rss_reader.di

import com.data.rssReader.service.RssDataService
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object RssDataApiRetrofitModule {
    @Provides
    fun provideDataDragonRetrofit(
        client: OkHttpClient,
        baseUrl: String,
        tikXmlFactory: TikXmlConverterFactory
    ): RssDataService = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(tikXmlFactory)
        .client(client)
        .build()
        .create(RssDataService::class.java)

//    @Provides
//    fun provideBaseUrl(): String = "https://www.google.com"
}