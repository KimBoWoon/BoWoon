package com.domain.rssReader.repository

import com.domain.rssReader.dto.Rss

interface RssDataRepository {
    suspend fun getRssData(url: String): Rss
}