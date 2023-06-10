package com.gps_alarm.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.data.gpsAlarm.local.LocalDataStore
import com.domain.gpsAlarm.usecase.DataStoreUseCase
import com.gps_alarm.base.BaseVM
import com.gps_alarm.data.SettingInfo
import com.gps_alarm.ui.util.decode
import com.gps_alarm.ui.util.encode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SettingVM @Inject constructor(
    private val dataStoreUseCase: DataStoreUseCase,
    private val json: Json
) : BaseVM(), ContainerHost<SettingInfo, SettingVM.SettingSideEffect> {
    override lateinit var container: Container<SettingInfo, SettingSideEffect>

    init {
        viewModelScope.launch {
            container = container(
                viewModelScope.async {
                    dataStoreUseCase.get(LocalDataStore.Keys.setting)?.decode(json) ?: SettingInfo()
                }.await()
            )
        }
    }

    sealed class SettingSideEffect {
        data class Save(val setting: String) : SettingSideEffect()
        data class ShowToast(val message: String) : SettingSideEffect()
    }

    enum class Setting {
        IS_FOLLOW,
        CIRCLE_SIZE
    }

    fun setSetting(name: Setting, value: Any) {
        intent {
            when (name) {
                Setting.IS_FOLLOW -> {
                    reduce { state.copy(isFollowing = value as Boolean) }
                }
                Setting.CIRCLE_SIZE -> {
                    reduce { state.copy(circleSize = value as Int) }
                }
            }
            postSideEffect(SettingSideEffect.Save(state.encode(json)))
        }
    }

    fun setDataStore(data: String) {
        intent {
            viewModelScope.launch {
                dataStoreUseCase.set(LocalDataStore.Keys.setting, data)
            }
            postSideEffect(SettingSideEffect.ShowToast("데이터 저장 완료"))
        }
    }
}