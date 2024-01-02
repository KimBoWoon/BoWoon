package com.bowoon.gps_alarm.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.bowoon.commonutils.Log
import com.bowoon.gpsAlarm.BuildConfig
import com.bowoon.gps_alarm.apis.Apis
import com.bowoon.gps_alarm.base.BaseVM
import com.bowoon.gps_alarm.data.Address
import com.bowoon.gps_alarm.data.Addresses
import com.bowoon.gps_alarm.data.Geocode
import com.bowoon.gps_alarm.data.GpsAlarmConstant
import com.bowoon.gps_alarm.ui.util.AlarmManager
import com.bowoon.gps_alarm.ui.util.dataMapper
import com.bowoon.commonutils.DataStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmVM @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val api: Apis,
    private val manager: AlarmManager
) : BaseVM() {
    companion object {
        private const val TAG = "#AlarmVM"
    }

    val geocode = MutableStateFlow<DataStatus<Geocode>>(DataStatus.Loading)
    val week = mutableSetOf<String>()
    val alarmList = MutableStateFlow<DataStatus<List<Address>?>>(DataStatus.Loading)

    init {
        if (BuildConfig.DEBUG) {
            val title = "스타벅스 석촌호수점"
            val addresses = Addresses(
                null,
                0.0,
                null,
                "서울 송파구 송파동 7-4",
                "서울 송파구 석촌호수로 262",
                127.10533937925041,
                37.50952059479555
            )
            val data = dataMapper(title, emptyList(), addresses)
            viewModelScope.launch { manager.add(data) }
        }
    }

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

    fun removeAlarm(address: Address) {
        viewModelScope.launch(Dispatchers.IO) {
            manager.remove(address)
        }
    }

    fun addAlarm(title: String, addresses: Addresses?) {
        viewModelScope.launch(Dispatchers.IO) {
            addresses?.let {
                dataMapper(title, week.toList(), addresses).run {
                    manager.add(this)
                }
            } ?: run {
                Log.d(TAG, "데이터를 확인해주세요!")
            }
        }
    }

    fun getGeocode(address: String) {
        flow {
            emit(
                api.mapsApi.getGeocode(
                    GpsAlarmConstant.geocodeUrl,
                    address
                )
            )
        }.onStart {
            geocode.value = DataStatus.Loading
        }.catch {e ->
            geocode.value = DataStatus.Failure(e)
        }.onEach {
            geocode.value = DataStatus.Success(it)
        }.launchIn(viewModelScope)
    }

    fun changeData(address: Address) {
        viewModelScope.launch {
            manager.add(address)
        }
    }
}