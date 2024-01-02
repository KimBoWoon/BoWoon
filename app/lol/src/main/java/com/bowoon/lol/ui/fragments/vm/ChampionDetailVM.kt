package com.bowoon.lol.ui.fragments.vm

import androidx.lifecycle.viewModelScope
import com.bowoon.lol.apis.Apis
import com.bowoon.lol.base.BaseVM
import com.bowoon.lol.data.ChampionDetailData
import com.bowoon.lol.data.LolDataConstant
import com.bowoon.commonutils.DataStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChampionDetailVM @Inject constructor(
    private val apis: Apis
) : BaseVM() {
    val champion = MutableStateFlow<DataStatus<ChampionDetailData?>>(DataStatus.Loading)

    fun getChampionInfo(version: String, name: String, language: String = "ko_KR") {
        viewModelScope.launch {
            runCatching {
                apis.dataDragonApi.getChampionInfo("${LolDataConstant.DATA_DRAGON_API_URL}/cdn/$version/data/$language/champion/$name.json")
            }.onSuccess { championInfo ->
                champion.value = DataStatus.Success(championInfo)
            }.onFailure { e ->
                champion.value = DataStatus.Failure(e)
            }
        }
    }
}