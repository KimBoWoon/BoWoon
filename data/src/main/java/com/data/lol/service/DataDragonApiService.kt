package com.data.lol.service

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
    ): ChampionRoot

    @GET("/cdn/{version}/data/{language}/champion/{name}.json")
    suspend fun getChampionInfo(
        @Path("version") version: String,
        @Path("language") language: String,
        @Path("name") name: String
    ): ChampionDetailRoot

    @GET("/cdn/{version}/data/{language}/item.json")
    suspend fun getAllGameItem(
        @Path("version") version: String,
        @Path("language") language: String
    ): GameItemRoot
}