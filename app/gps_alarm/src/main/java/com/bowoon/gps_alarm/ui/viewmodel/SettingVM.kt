package com.bowoon.gps_alarm.ui.viewmodel

import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.viewModelScope
import com.bowoon.commonutils.DataStatus
import com.bowoon.datamanager.DataStoreRepository
import com.bowoon.gps_alarm.base.BaseVM
import com.bowoon.gps_alarm.data.SettingInfo
import com.bowoon.gps_alarm.ui.util.decode
import com.bowoon.gps_alarm.ui.util.encode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class SettingVM @Inject constructor(
    private val json: Json,
    private val dataStoreRepository: DataStoreRepository
) : BaseVM() {
    val setting = MutableStateFlow<DataStatus<SettingInfo?>>(DataStatus.Loading)

    enum class Setting {
        IS_FOLLOW,
        CIRCLE_SIZE
    }

    init {
        fetchSetting()
    }

    fun fetchSetting() {
        flow {
            dataStoreRepository.getData(
                DataStoreRepository.GPS_ALARM_DATA_STORE_NAME,
                stringPreferencesKey("settingInfo"),
                ""
            ).also {
                if (it.isNullOrEmpty()) {
                    emit(SettingInfo())
                } else {
                    emit(it.decode(json))
                }
            }
        }.onStart {
            setting.value = DataStatus.Loading
        }.catch { e ->
            setting.value = DataStatus.Failure(e)
        }.onEach {
            setting.value = DataStatus.Success(it)
        }.launchIn(viewModelScope)
    }

    fun saveSetting(setting: SettingInfo) {
        viewModelScope.launch {
            dataStoreRepository.setData(
                DataStoreRepository.GPS_ALARM_DATA_STORE_NAME,
                stringPreferencesKey("settingInfo"),
                setting.encode(json)
            )
        }
    }
}