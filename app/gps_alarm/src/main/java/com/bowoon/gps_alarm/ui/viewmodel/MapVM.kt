package com.bowoon.gps_alarm.ui.viewmodel

import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.viewModelScope
import com.bowoon.commonutils.DataStatus
import com.bowoon.commonutils.Log
import com.bowoon.datamanager.DataStoreRepository
import com.bowoon.gps_alarm.base.BaseVM
import com.bowoon.gps_alarm.data.Address
import com.bowoon.gps_alarm.data.SettingInfo
import com.bowoon.gps_alarm.ui.util.AlarmManager
import com.bowoon.gps_alarm.ui.util.decode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class MapVM @Inject constructor(
    private val json: Json,
    private val manager: AlarmManager,
    private val dataStoreRepository: DataStoreRepository
) : BaseVM() {
    val alarmList = MutableStateFlow<DataStatus<List<Address>?>>(DataStatus.Loading)
    val setting = MutableStateFlow<DataStatus<SettingInfo?>>(DataStatus.Loading)

    fun fetchAlarmList() {
        viewModelScope.launch(Dispatchers.IO) {
            flow {
                emit(manager.getList())
            }.onStart { alarmList.value = DataStatus.Loading }
                .catch { e -> alarmList.value = DataStatus.Failure(e) }
                .onEach {
                    if (it.isEmpty()) {
                        alarmList.value = DataStatus.Success(emptyList())
                    } else {
                        alarmList.value = DataStatus.Success(it)
                    }
                }.launchIn(viewModelScope)
        }
    }

    fun fetchSetting() {
        flow {
            emit(
                dataStoreRepository.getData(
                    DataStoreRepository.GPS_ALARM_DATA_STORE_NAME,
                    stringPreferencesKey("settingInfo"),
                    ""
                ) ?: ""
            )
        }.catch { e -> Log.printStackTrace(e) }
            .onEach {
                if (it.trim().isNotEmpty()) {
                    setting.value = DataStatus.Success(it.decode(json))
                }
            }
            .onCompletion {
                fetchAlarmList()
            }
            .launchIn(viewModelScope)
    }
}