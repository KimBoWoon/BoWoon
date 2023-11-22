package com.data.lol.service

import com.data.lol.dto.ChampionData
import com.data.lol.dto.ChampionDetailData
import com.data.lol.dto.GameItemData
import com.domain.lol.dto.ChampionDetailRoot
import com.domain.lol.dto.ChampionRoot
import com.domain.lol.dto.GameItemRoot
import retrofit2.http.GET
import retrofit2.http.Path

interface DataDragonApiService {
    @GET("/api/versions.json")
    suspend fun getVersion(): List<String>

    @GET("/cdn/{version}/data/{language}/champion.json")
    suspend fun getAllChampion(
        @Path("version") version: String,
        @Path("language") language: String
    ): ChampionData

    @GET("/cdn/{version}/data/{language}/champion/{name}.json")
    suspend fun getChampionInfo(
        @Path("version") version: String,
        @Path("language") language: String,
        @Path("name") name: String
    ): ChampionDetailData

    @GET("/cdn/{version}/data/{language}/item.json")
    suspend fun getAllGameItem(
        @Path("version") version: String,
        @Path("language") language: String
    ): GameItemData
}