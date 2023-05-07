package com.rss_reader.di

import com.domain.rssReader.repository.RssDataRepository
import com.domain.rssReader.usecase.RssDataUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    @Provides
    fun provideRssDataUseCase(
        rssDataRepository: RssDataRepository
    ): RssDataUseCase = RssDataUseCase(rssDataRepository)
}