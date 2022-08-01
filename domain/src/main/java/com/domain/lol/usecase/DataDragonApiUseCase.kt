package com.domain.lol.usecase

import com.domain.lol.dto.ChampionRoot
import com.domain.lol.repository.DataDragonApiRepository

class DataDragonApiUseCase(
    private val dataDragonApiRepository: DataDragonApiRepository
) {
    suspend fun getVersion(): List<String> = dataDragonApiRepository.getVersion()
    suspend fun getAllChampion(
        version: String,
        language: String
    ): ChampionRoot = dataDragonApiRepository.getAllChampion(version, language)
}