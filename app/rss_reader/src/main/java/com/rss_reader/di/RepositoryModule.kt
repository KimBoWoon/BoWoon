package com.rss_reader.di

import com.data.rssReader.repository.RssDataRepositoryImpl
import com.data.rssReader.service.RssDataService
import com.domain.rssReader.repository.RssDataRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {
    @Provides
    fun provideRssDataRepository(
        rssDataService: RssDataService
    ): RssDataRepository = RssDataRepositoryImpl(rssDataService)
}