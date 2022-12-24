package com.lol.ui.fragments.vm

import androidx.lifecycle.viewModelScope
import com.domain.lol.dto.ChampionDetailRoot
import com.domain.gpsAlarm.usecase.DataDragonApiUseCase
import com.lol.base.BaseVM
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import util.DataStatus
import javax.inject.Inject

@HiltViewModel
class ChampionDetailVM @Inject constructor(
    private val dataDragonApiUseCase: DataDragonApiUseCase
) : BaseVM() {
    val champion = MutableStateFlow<DataStatus<ChampionDetailRoot?>>(DataStatus.Loading)

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