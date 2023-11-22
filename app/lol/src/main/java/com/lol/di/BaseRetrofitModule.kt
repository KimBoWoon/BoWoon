package com.lol.di

import com.data.BuildConfig
import com.lol.network.AppInterceptor
import com.lol.network.NetworkLogInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient

@Module
@InstallIn(SingletonComponent::class)
object BaseRetrofitModule {
    @Provides
    fun provideOkHttpClient(
        interceptor: okhttp3.logging.HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient().newBuilder().apply {
        addNetworkInterceptor(interceptor)
        addInterceptor(AppInterceptor())
        if (BuildConfig.DEBUG) {
            addInterceptor(com.localebro.okhttpprofiler.OkHttpProfilerInterceptor())
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
    fun provideInterceptor(): okhttp3.logging.HttpLoggingInterceptor = okhttp3.logging.HttpLoggingInterceptor()
        .apply {
        level = okhttp3.logging.HttpLoggingInterceptor.Level.BODY
    }
}