package com.data.base.di

import com.data.BuildConfig
import com.data.base.util.NetworkLogInterceptor
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
        level = HttpLoggingInterceptor.Level.NONE
    }
}