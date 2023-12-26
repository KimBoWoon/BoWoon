package com.bowoon.gps_alarm.ui.viewmodel

import com.bowoon.gps_alarm.base.BaseVM
import com.domain.gpsAlarm.usecase.DataStoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class SettingVM @Inject constructor(
    private val dataStoreUseCase: DataStoreUseCase,
    private val json: Json
) : BaseVM() {
//    override lateinit var container: Container<SettingInfo, SettingSideEffect>

    init {
//        viewModelScope.launch {
//            container = container(
//                viewModelScope.async {
//                    dataStoreUseCase.get(LocalDataStore.Keys.setting)?.decode(json) ?: SettingInfo()
//                }.await()
//            )
//        }
    }

    sealed class SettingSideEffect {
        data class Save(val setting: String) : SettingSideEffect()
        data class ShowToast(val message: String) : SettingSideEffect()
    }

    enum class Setting {
        IS_FOLLOW,
        CIRCLE_SIZE
    }

//    fun setSetting(name: Setting, value: Any) {
//        intent {
//            when (name) {
//                Setting.IS_FOLLOW -> {
//                    reduce { state.copy(isFollowing = value as Boolean) }
//                }
//                Setting.CIRCLE_SIZE -> {
//                    reduce { state.copy(circleSize = value as Int) }
//                }
//            }
//            postSideEffect(SettingSideEffect.Save(state.encode(json)))
//        }
//    }
//
//    fun setDataStore(data: String) {
//        intent {
//            viewModelScope.launch {
//                dataStoreUseCase.set(LocalDataStore.Keys.setting, data)
//            }
//            postSideEffect(SettingSideEffect.ShowToast("데이터 저장 완료"))
//        }
//    }
}