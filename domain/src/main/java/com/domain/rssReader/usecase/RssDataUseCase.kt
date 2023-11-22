package com.domain.rssReader.usecase

import com.domain.rssReader.dto.Rss
import com.domain.rssReader.repository.RssDataRepository

class RssDataUseCase(
    private val rssDataRepository: RssDataRepository
) {
    suspend fun getRssData(url: String): Rss = rssDataRepository.getRssData(url)
}