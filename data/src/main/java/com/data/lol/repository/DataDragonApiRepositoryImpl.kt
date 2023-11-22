package com.data.lol.repository

import com.data.lol.mapper.convert
import com.data.lol.service.DataDragonApiService
import com.domain.lol.dto.ChampionDetailRoot
import com.domain.lol.dto.ChampionRoot
import com.domain.lol.dto.GameItemRoot
import com.domain.lol.repository.DataDragonApiRepository

class DataDragonApiRepositoryImpl(
    private val dataDragonApiService: DataDragonApiService
) : DataDragonApiRepository {
    override suspend fun getVersion(): List<String> = dataDragonApiService.getVersion()
    override suspend fun getAllChampion(version: String, language: String): ChampionRoot? =
        dataDragonApiService.getAllChampion(version, language).convert()
    override suspend fun getChampionInfo(version: String, language: String, name: String): ChampionDetailRoot? =
        dataDragonApiService.getChampionInfo(version, language, name).convert()
    override suspend fun getAllGameItem(version: String, language: String): GameItemRoot? =
        dataDragonApiService.getAllGameItem(version, language).convert()
}