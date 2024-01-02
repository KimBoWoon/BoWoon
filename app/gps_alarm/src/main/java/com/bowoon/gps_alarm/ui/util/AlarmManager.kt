package com.bowoon.gps_alarm.ui.util

import androidx.datastore.preferences.core.stringPreferencesKey
import com.bowoon.datamanager.DataStoreRepository
import com.bowoon.gps_alarm.data.Address
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmManager @Inject constructor(
    private val json: Json,
    private val dataStoreRepository: DataStoreRepository
) {
    private val alarmList = mutableListOf<Address>()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            dataStoreRepository.getData(
                DataStoreRepository.GPS_ALARM_DATA_STORE_NAME,
                stringPreferencesKey("ALARM_LIST"),
                ""
            )?.also {
                if (it.isNotEmpty()) {
                    alarmList.addAll(json.decodeFromString<List<Address>>(it))
                }
            }
        }
    }

    suspend fun add(address: Address) {
        alarmList.find { it.longitude == address.longitude && it.latitude == address.latitude }
            ?.let {
                alarmList.remove(it)
            }

        alarmList.add(address)
        dataStoreRepository.setData(
            DataStoreRepository.GPS_ALARM_DATA_STORE_NAME,
            stringPreferencesKey("ALARM_LIST"),
            json.encodeToString(alarmList)
        )
    }

    suspend fun remove(address: Address) {
        alarmList.remove(address)
        dataStoreRepository.setData(
            DataStoreRepository.GPS_ALARM_DATA_STORE_NAME,
            stringPreferencesKey("ALARM_LIST"),
            json.encodeToString(alarmList)
        )
    }

    fun getList(): List<Address> = alarmList

    suspend fun clear() {
        alarmList.clear()
        dataStoreRepository.setData(
            DataStoreRepository.GPS_ALARM_DATA_STORE_NAME,
            stringPreferencesKey("ALARM_LIST"),
            ""
        )
    }
}