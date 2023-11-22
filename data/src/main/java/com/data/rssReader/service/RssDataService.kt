package com.data.rssReader.service

import com.data.rssReader.dto.Rss
import retrofit2.http.GET
import retrofit2.http.Url

interface RssDataService {
    @GET
    suspend fun getRss(@Url url: String): Rss
}