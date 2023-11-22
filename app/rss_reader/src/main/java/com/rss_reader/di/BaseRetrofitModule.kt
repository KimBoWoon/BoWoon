package com.rss_reader.di

import com.data.BuildConfig
import com.gps_alarm.service.AppInterceptor
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
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
        addInterceptor(com.gps_alarm.service.AppInterceptor())
        if (BuildConfig.DEBUG) {
            addInterceptor(OkHttpProfilerInterceptor())
            addInterceptor(NetworkLogInterceptor())
        }
    }.build()

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
}