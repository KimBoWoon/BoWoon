package com.domain.lol.usecase

import com.domain.lol.dto.ChampionDetailRoot
import com.domain.lol.dto.ChampionRoot
import com.domain.lol.dto.GameItemRoot
import com.domain.lol.repository.DataDragonApiRepository

class DataDragonApiUseCase(
    private val dataDragonApiRepository: DataDragonApiRepository
) {
    suspend fun getVersion(): List<String> = dataDragonApiRepository.getVersion()
    suspend fun getAllChampion(version: String, language: String): ChampionRoot =
        dataDragonApiRepository.getAllChampion(version, language)
    suspend fun getChampionInfo(version: String, language: String, name: String): ChampionDetailRoot =
        dataDragonApiRepository.getChampionInfo(version, language, name)
    suspend fun getAllGameItem(version: String, language: String): GameItemRoot =
        dataDragonApiRepository.getAllGameItem(version, language)
}