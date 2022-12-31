package com.gps_alarm.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.data.gpsAlarm.local.LocalDatastore
import com.gps_alarm.base.BaseVM
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class MapVM @Inject constructor(
    private val localDatastore: LocalDatastore
) : BaseVM() {
    private val json = Json { ignoreUnknownKeys = true }
    val addressList = mutableStateOf<List<com.gps_alarm.data.Address>>(emptyList())

    init {
        getAddress()
    }

    private fun getAddress() {
        viewModelScope.launch {
            addressList.value = localDatastore.get(LocalDatastore.Keys.alarmList)?.let { addressesSet ->
                addressesSet.map {
                    val address = json.decodeFromString<com.gps_alarm.data.Address>(it)
                    com.gps_alarm.data.Address(
                        address.name,
                        address.distance,
                        address.englishAddress,
                        address.jibunAddress,
                        address.roadAddress,
                        address.longitude,
                        address.latitude
                    )
                }
            } ?: emptyList()
        }
    }
}