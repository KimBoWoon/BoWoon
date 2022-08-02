package com.lol.ui.vm

import androidx.lifecycle.viewModelScope
import com.data.base.util.DataStatus
import com.domain.lol.dto.ChampionDetailRoot
import com.domain.lol.dto.ChampionRoot
import com.domain.lol.usecase.DataDragonApiUseCase
import com.domain.lol.usecase.RiotApiUseCase
import com.lol.base.BaseVM
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainVM @Inject constructor(
    private val riotApiUseCase: RiotApiUseCase,
    private val dataDragonApiUseCase: DataDragonApiUseCase
) : BaseVM() {
    val lolVersion = MutableStateFlow<DataStatus<List<String>>>(DataStatus.Loading)
    val allChampion = MutableStateFlow<DataStatus<ChampionRoot>>(DataStatus.Loading)
    val champion = MutableStateFlow<DataStatus<ChampionDetailRoot>>(DataStatus.Loading)

    init {
        viewModelScope.launch {
            runCatching {
                dataDragonApiUseCase.getVersion()
            }.onSuccess { lolVersionList ->
                lolVersion.value = DataStatus.Success(lolVersionList)
            }.onFailure { e ->
                lolVersion.value = DataStatus.Failure(e)
            }
        }
    }

    fun getAllChampion(version: String, language: String = "ko_KR") {
        viewModelScope.launch {
            runCatching {
                dataDragonApiUseCase.getAllChampion(version, language)
            }.onSuccess { champion ->
                allChampion.value = DataStatus.Success(champion)
            }.onFailure { e ->
                allChampion.value = DataStatus.Failure(e)
            }
        }
    }

    fun getChampionInfo(version: String, name: String, language: String = "ko_KR") {
        viewModelScope.launch {
            runCatching {
                dataDragonApiUseCase.getChampionInfo(version, language, name)
            }.onSuccess { championInfo ->
                champion.value = DataStatus.Success(championInfo)
            }.onFailure { e ->
                champion.value = DataStatus.Failure(e)
            }
        }
    }
}