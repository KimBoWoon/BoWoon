package com.data.rssReader.di

import com.data.BuildConfig
import com.data.rssReader.service.AppInterceptor
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import util.NetworkLogInterceptor

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