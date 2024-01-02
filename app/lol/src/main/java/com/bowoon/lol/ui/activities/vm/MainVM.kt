package com.bowoon.lol.ui.activities.vm

import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.viewModelScope
import com.bowoon.datamanager.DataStoreRepository
import com.bowoon.lol.apis.Apis
import com.bowoon.lol.base.BaseVM
import com.bowoon.lol.data.ChampionData
import com.bowoon.lol.data.GameItemData
import com.bowoon.lol.data.LolDataConstant
import com.bowoon.commonutils.DataStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainVM @Inject constructor(
    private val apis: Apis,
    private val dataStoreRepository: DataStoreRepository
) : BaseVM() {
    val lolVersion = MutableStateFlow<DataStatus<String?>>(DataStatus.Loading)
    val lolVersionList = MutableStateFlow<DataStatus<List<String>>>(DataStatus.Loading)
    val allChampion = MutableStateFlow<DataStatus<ChampionData?>>(DataStatus.Loading)
    val allGameItem = MutableStateFlow<DataStatus<GameItemData?>>(DataStatus.Loading)

    init {
        viewModelScope.launch {
            runCatching {
                apis.dataDragonApi.getVersion("${LolDataConstant.DATA_DRAGON_API_URL}/api/versions.json")
            }.onSuccess { versionList ->
                lolVersion.value = dataStoreRepository.getData(LolDataConstant.LOL_DATA_STORE_NAME, stringPreferencesKey("LOL_VERSION"), null)?.let {
                    DataStatus.Success(it)
                } ?: run {
                    dataStoreRepository.setData(LolDataConstant.LOL_DATA_STORE_NAME, stringPreferencesKey("LOL_VERSION"), versionList.firstOrNull() ?: "UNKNOWN VERSION")
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
                apis.dataDragonApi.getAllChampion("${LolDataConstant.DATA_DRAGON_API_URL}/cdn/$version/data/$language/champion.json")
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
                apis.dataDragonApi.getAllGameItem("${LolDataConstant.DATA_DRAGON_API_URL}/cdn/$version/data/$language/item.json")
            }.onSuccess { gameItem ->
                allGameItem.value = DataStatus.Success(gameItem)
            }.onFailure { e ->
                allGameItem.value = DataStatus.Failure(e)
            }
        }
    }

    fun changeVersion() {
        viewModelScope.launch {
            val version = dataStoreRepository.getData(LolDataConstant.LOL_DATA_STORE_NAME, stringPreferencesKey("LOL_VERSION"), null)
            if ((lolVersion.value as? DataStatus.Success<String?>)?.data != version) {
                lolVersion.value = DataStatus.Success(version)
            }
        }
    }
}