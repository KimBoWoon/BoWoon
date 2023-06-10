package com.gps_alarm.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.data.gpsAlarm.local.LocalDataStore
import com.domain.gpsAlarm.usecase.DataStoreUseCase
import com.gps_alarm.base.BaseVM
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class GpsAlarmVM @Inject constructor(
    private val json: Json,
    private val dataStoreUseCase: DataStoreUseCase
) : BaseVM() {
    var appBarTitle = mutableStateOf("GPS 알람")

    suspend fun getAddressList(): Array<String> =
        viewModelScope.async {
            dataStoreUseCase.get(LocalDataStore.Keys.alarmList)?.toTypedArray()
        }.await() ?: emptyArray()
}