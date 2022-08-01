package com.data.lol.repository

import com.data.lol.service.DataDragonApiService
import com.domain.lol.dto.ChampionRoot
import com.domain.lol.repository.DataDragonApiRepository

class DataDragonApiRepositoryImpl(
    private val dataDragonApiService: DataDragonApiService
) : DataDragonApiRepository {
    override suspend fun getVersion(): List<String> = dataDragonApiService.getVersion()
    override suspend fun getAllChampion(
        version: String,
        language: String
    ): ChampionRoot = dataDragonApiService.getAllChampion(version, language)
}