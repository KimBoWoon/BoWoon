package com.gps_alarm.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.data.gpsAlarm.local.LocalDatastore
import com.data.gpsAlarm.mapper.mapper
import com.domain.gpsAlarm.dto.Addresses
import com.domain.gpsAlarm.dto.Geocode
import com.domain.gpsAlarm.usecase.MapsApiUseCase
import com.gps_alarm.base.BaseVM
import com.gps_alarm.data.Address
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
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
    private val json: Json
) : BaseVM() {
    val geocodeList = MutableStateFlow<DataStatus<List<Address>>>(DataStatus.Loading)
    val findAddress = MutableStateFlow<Address?>(null)
    val geocode = mutableStateOf<Geocode?>(null)

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
                        val data = json.decodeFromString<Address>(it)
                        data.longitude == addresses.longitude && data.latitude == addresses.latitude
                    }?.let {
                        val result = mutableSetOf<String>()
                        val decodeString = json.decodeFromString<com.data.gpsAlarm.dto.Addresses>(it)
                        val data = json.encodeToString(dataMapper(alarmTitle, decodeString.mapper()))
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

    fun changeData(address: Address) {
        viewModelScope.launch {
            localDatastore.get(LocalDatastore.Keys.alarmList)?.let { addressesSet ->
                addressesSet.find {
                    val data = json.decodeFromString<Address>(it)
                    data.longitude == address.longitude && data.latitude == address.latitude
                }?.let {
                    val result = mutableSetOf<String>()
                    val encodeString = json.encodeToString(address)
                    addressesSet.forEach {
                        val decodeString = json.decodeFromString<Address>(it)
                        if (decodeString.longitude == address.longitude && decodeString.latitude == address.latitude) {
                            result.add(encodeString)
                        } else {
                            result.add(it)
                        }
                    }
                    localDatastore.set(LocalDatastore.Keys.alarmList, result)
                }
            }
        }
    }

    fun setList() {
        viewModelScope.launch {
            callbackFlow {
                val data = localDatastore.get(LocalDatastore.Keys.alarmList)?.let { addressesSet ->
                    val result = mutableListOf<Address>()
                    addressesSet.forEach {
                        val address = json.decodeFromString<Address>(it)
                        result.add(address)
                    }
                    Log.d(result.toString())
                    trySend(result)
                } ?: run {
                    trySend(emptyList())
                    Log.d("setList is null")
                }

                awaitClose {
                    Log.d("setList callbackFlow close...")
                }
            }.onStart {
                geocodeList.value = DataStatus.Loading
            }.onEmpty {
                geocodeList.value = DataStatus.Success(emptyList())
            }.catch {
                geocodeList.value = DataStatus.Failure(it)
            }.collect {
                geocodeList.value = DataStatus.Success(it)
            }
        }
    }

    private fun dataMapper(alarmTitle: String, addresses: Addresses): Address =
        Address(
            alarmTitle,
            false,
            addresses.distance,
            addresses.englishAddress,
            addresses.jibunAddress,
            addresses.roadAddress,
            addresses.longitude,
            addresses.latitude
        )

    fun getAddress(longitude: String, latitude: String) {
        viewModelScope.launch {
            callbackFlow {
                viewModelScope.launch {
                    localDatastore.get(LocalDatastore.Keys.alarmList)?.let { addressSet ->
                        addressSet.find {
                            val data = json.decodeFromString<Address>(it)
                            data.longitude == longitude.toDouble() && data.latitude == latitude.toDouble()
                        }?.let {
                            trySend(it)
                        }
                    }
                }

                awaitClose {
                    Log.d("getAddress callbackFlow close...")
                }
            }.onStart {
                findAddress.value = null
            }.map {
                json.decodeFromString<Address>(it)
            }.collect {
                findAddress.value = it
            }
        }
    }
}