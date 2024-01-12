package com.bowoon.rss_reader.apis

import com.bowoon.network.ApiResponse
import com.bowoon.rss_reader.data.Rss
import retrofit2.http.GET
import retrofit2.http.Url

interface RssDataService {
    @GET
    suspend fun getRss(@Url url: String): ApiResponse<Rss>
}