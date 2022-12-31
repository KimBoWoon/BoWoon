package com.gps_alarm.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.data.gpsAlarm.local.LocalDatastore
import com.domain.gpsAlarm.dto.Addresses
import com.domain.gpsAlarm.dto.Geocode
import com.domain.gpsAlarm.usecase.MapsApiUseCase
import com.gps_alarm.base.BaseVM
import com.gps_alarm.paging.room.AppDatabase
import com.gps_alarm.paging.source.GeocodeSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import util.DataStatus
import util.Log
import util.coroutineIOCallbackTo
import javax.inject.Inject

@HiltViewModel
class AlarmVM @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val mapsApiUseCase: MapsApiUseCase,
    private val localDatastore: LocalDatastore,
    private val database: AppDatabase
) : BaseVM() {
    private val json = Json { ignoreUnknownKeys = true }
    val geocodeList = MutableStateFlow<DataStatus<List<com.gps_alarm.data.Address>>>(DataStatus.Loading)
    val addressDao = database.addressDao()
    val geocode = mutableStateOf<Geocode?>(null)

    @ExperimentalPagingApi
    val pager = Pager(
        config = PagingConfig(pageSize = 20, initialLoadSize = 20, prefetchDistance = 5),
        remoteMediator = GeocodeSource(localDatastore, database)
    ) {
        addressDao.pagingSource()
    }.flow

    init {
        setList()
    }

    fun getGeocode(address: String) {
        viewModelScope.launch {
            coroutineIOCallbackTo(
                block = { mapsApiUseCase.getGeocode(address) },
                success = { geocode.value = it },
                error = { e -> Log.printStackTrace(e) }
            )
        }
    }

    fun setDataStore(alarmTitle: String, addresses: List<Addresses>?) {
        viewModelScope.launch {
            addresses?.firstOrNull()?.let { addresses ->
                localDatastore.get(LocalDatastore.Keys.alarmList)?.let { addressesSet ->
                    addressesSet.find {
                        val data = json.decodeFromString<com.gps_alarm.data.Address>(it)
                        data.longitude == addresses.longitude && data.latitude == addresses.latitude
                    }?.let {
                        val result = mutableSetOf<String>()
                        val decodeString = json.decodeFromString<Addresses>(it)
                        val data = json.encodeToString(dataMapper(alarmTitle, decodeString))
                        result.addAll(addressesSet)
                        result.add(data)
                        localDatastore.set(LocalDatastore.Keys.alarmList, result)
                    } ?: run {
                        val data = dataMapper(alarmTitle, addresses)
                        val result = mutableSetOf<String>()
                        result.addAll(addressesSet)
                        result.add(json.encodeToString(data))
                        localDatastore.set(LocalDatastore.Keys.alarmList, result)
                    }
                } ?: run {
                    val data = dataMapper(alarmTitle, addresses)
                    val result = mutableSetOf<String>()
                    result.add(json.encodeToString(data))
                    localDatastore.set(LocalDatastore.Keys.alarmList, result)
                }
            }
        }
    }

    fun setList() {
        geocodeList.value = DataStatus.Loading
        viewModelScope.launch {
            localDatastore.get(LocalDatastore.Keys.alarmList)?.let { addressesSet ->
                val result = mutableListOf<com.gps_alarm.data.Address>()
                addressesSet.forEach {
                    val address = json.decodeFromString<com.gps_alarm.data.Address>(it)
                    result.add(address)
                }
                geocodeList.value = DataStatus.Success(result)
            } ?: run {
                geocodeList.value = DataStatus.Success(emptyList())
            }
        }
    }

    private fun dataMapper(alarmTitle: String, addresses: Addresses): com.gps_alarm.data.Address =
        com.gps_alarm.data.Address(
            alarmTitle,
            addresses.distance,
            addresses.englishAddress,
            addresses.jibunAddress,
            addresses.roadAddress,
            addresses.longitude,
            addresses.latitude
        )

    suspend fun getAddress(addressId: Int): com.gps_alarm.data.Address = addressDao.getAddress(addressId)
}