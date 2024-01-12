package com.bowoon.network

import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object BaseRetrofitModule {
    @Provides
    fun provideOkHttpClient(
        interceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient().newBuilder().apply {
        connectTimeout(1, TimeUnit.MINUTES)
        readTimeout(30, TimeUnit.SECONDS)
        writeTimeout(15, TimeUnit.SECONDS)
        addNetworkInterceptor(interceptor)
        addInterceptor(AppInterceptor())
        if (BuildConfig.DEBUG) {
            addInterceptor(OkHttpProfilerInterceptor())
            addInterceptor(NetworkLogInterceptor())
        }
    }.build()

    @Provides
    fun provideCustomCallAdapter(): CustomCallAdapter = CustomCallAdapter()

    @Provides
    fun provideKotlinSerialization(): Json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    @Provides
    fun provideJsonMediaType(): MediaType = "application/json".toMediaType()

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