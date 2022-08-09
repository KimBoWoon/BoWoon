package com.lol.ui.activities.vm

import androidx.lifecycle.viewModelScope
import com.domain.lol.dto.ChampionRoot
import com.domain.lol.dto.GameItemRoot
import com.domain.lol.usecase.DataDragonApiUseCase
import com.lol.base.BaseVM
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import util.DataStatus
import javax.inject.Inject

@HiltViewModel
class MainVM @Inject constructor(
    private val dataDragonApiUseCase: DataDragonApiUseCase
) : BaseVM() {
    val lolVersion = MutableStateFlow<DataStatus<List<String>>>(DataStatus.Loading)
    val allChampion = MutableStateFlow<DataStatus<ChampionRoot>>(DataStatus.Loading)
    val allGameItem = MutableStateFlow<DataStatus<GameItemRoot>>(DataStatus.Loading)

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

    fun getAllGameItem(version: String, language: String = "ko_KR") {
        viewModelScope.launch {
            runCatching {
                dataDragonApiUseCase.getAllGameItem(version, language)
            }.onSuccess { gameItem ->
                allGameItem.value = DataStatus.Success(gameItem)
            }.onFailure { e ->
                allGameItem.value = DataStatus.Failure(e)
            }
        }
    }
}