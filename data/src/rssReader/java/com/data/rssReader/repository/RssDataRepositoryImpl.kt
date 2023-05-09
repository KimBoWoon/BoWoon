package com.data.rssReader.repository

import com.data.rssReader.service.RssDataService
import com.domain.rssReader.dto.Rss
import com.domain.rssReader.repository.RssDataRepository

class RssDataRepositoryImpl(
    private val rssDataService: RssDataService
) : RssDataRepository {
    override suspend fun getRssData(url: String): Rss =
        rssDataService.getRss(url).mapper()
}