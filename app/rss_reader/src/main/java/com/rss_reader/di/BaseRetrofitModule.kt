package com.rss_reader.di

import com.data.BuildConfig
import com.rss_reader.network.AppInterceptor
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import com.rss_reader.network.NetworkLogInterceptor
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

@Module
@InstallIn(SingletonComponent::class)
object BaseRetrofitModule {
    @Provides
    fun provideOkHttpClient(
        interceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient().newBuilder().apply {
        addNetworkInterceptor(interceptor)
        addInterceptor(AppInterceptor())
        if (BuildConfig.DEBUG) {
            addInterceptor(OkHttpProfilerInterceptor())
            addInterceptor(NetworkLogInterceptor())
        }
    }.build()

    @Provides
    fun provideInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    fun provideTikXmlFactory(tikXml: TikXml): TikXmlConverterFactory =
        TikXmlConverterFactory.create(tikXml)

    @Provides
    fun provideTikXml(): TikXml = TikXml.Builder().exceptionOnUnreadXml(false).build()
}