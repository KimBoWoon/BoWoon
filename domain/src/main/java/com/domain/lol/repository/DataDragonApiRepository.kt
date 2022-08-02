package com.domain.lol.repository

import com.domain.lol.dto.ChampionDetailRoot
import com.domain.lol.dto.ChampionRoot

interface DataDragonApiRepository {
    suspend fun getVersion(): List<String>
    suspend fun getAllChampion(version: String, language: String): ChampionRoot
    suspend fun getChampionInfo(version: String, language: String, name: String): ChampionDetailRoot
}