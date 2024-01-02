package com.bowoon.lol.apis

import com.bowoon.lol.data.ChampionData
import com.bowoon.lol.data.ChampionDetailData
import com.bowoon.lol.data.GameItemData
import retrofit2.http.GET
import retrofit2.http.Url

interface DataDragonApiService {
    @GET
    suspend fun getVersion(
        @Url url: String
    ): List<String>

    @GET
    suspend fun getAllChampion(
        @Url url: String
    ): ChampionData

    @GET
    suspend fun getChampionInfo(
        @Url url: String
    ): ChampionDetailData

    @GET
    suspend fun getAllGameItem(
        @Url url: String
    ): GameItemData
}