package com.lol.ui.activities.vm

import androidx.lifecycle.viewModelScope
import com.data.gpsAlarm.local.LocalDataStore
import com.domain.rssReader.dto.ChampionRoot
import com.domain.lol.dto.GameItemRoot
import com.domain.gpsAlarm.usecase.DataDragonApiUseCase
import com.lol.base.BaseVM
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import util.DataStatus
import javax.inject.Inject

@HiltViewModel
class MainVM @Inject constructor(
    private val dataDragonApiUseCase: DataDragonApiUseCase,
    private val dataStoreUseCase: DataStoreUseCase
) : BaseVM() {
    val lolVersion = MutableStateFlow<DataStatus<String?>>(DataStatus.Loading)
    val lolVersionList = MutableStateFlow<DataStatus<List<String>>>(DataStatus.Loading)
    val allChampion = MutableStateFlow<DataStatus<ChampionRoot?>>(DataStatus.Loading)
    val allGameItem = MutableStateFlow<DataStatus<GameItemRoot?>>(DataStatus.Loading)

    init {
        viewModelScope.launch {
            runCatching {
                dataDragonApiUseCase.getVersion()
            }.onSuccess { versionList ->
                lolVersion.value = dataStoreUseCase.get(LocalDataStore.Keys.LOL_VERSION)?.let {
                    DataStatus.Success(it)
                } ?: run {
                    dataStoreUseCase.set(LocalDataStore.Keys.LOL_VERSION, versionList.firstOrNull() ?: "UNKNOWN VERSION")
                    DataStatus.Success(versionList.firstOrNull())
                }
                lolVersionList.value = DataStatus.Success(versionList)
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

    fun changeVersion() {
        viewModelScope.launch {
            val version = dataStoreUseCase.get(LocalDataStore.Keys.LOL_VERSION)
            if ((lolVersion.value as? DataStatus.Success<String?>)?.data != version) {
                lolVersion.value = DataStatus.Success(version)
            }
        }
    }
}