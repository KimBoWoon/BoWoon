package com.bowoon.component.di

import com.bowoon.component.utils.ComponentUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ComponentModule {
    @Provides
    fun provideComponentUtils(): ComponentUtils = ComponentUtils()
}