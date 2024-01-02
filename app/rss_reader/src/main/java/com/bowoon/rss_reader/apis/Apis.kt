package com.bowoon.rss_reader.apis

import com.bowoon.network.XmlConverterRetrofit
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Apis @Inject constructor(
    @XmlConverterRetrofit private val retrofit: Retrofit
) {
    val rssApi = retrofit.create(RssDataService::class.java)
}