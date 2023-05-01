package com.gps_alarm.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.data.gpsAlarm.local.LocalDatastore
import com.data.gpsAlarm.mapper.mapper
import com.domain.gpsAlarm.dto.Addresses
import com.domain.gpsAlarm.dto.Geocode
import com.domain.gpsAlarm.usecase.MapsApiUseCase
import com.domain.gpsAlarm.utils.FlowCallback
import com.gps_alarm.base.BaseVM
import com.gps_alarm.data.Address
import com.gps_alarm.data.AlarmData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import util.Log
import util.coroutineIOCallbackTo
import javax.inject.Inject

@HiltViewModel
class AlarmVM @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val mapsApiUseCase: MapsApiUseCase,
    private val localDatastore: LocalDatastore,
    private val json: Json
) : ContainerHost<AlarmData, String>, BaseVM() {
    val findAddress = MutableStateFlow<Address?>(null)
    val geocode = MutableStateFlow<Geocode?>(null)
    val chooseAddress = MutableStateFlow<Addresses?>(null)
    val alarmTitle = MutableStateFlow<String>("")

    override val container: Container<AlarmData, String> = container(AlarmData())

    init {
        fetchAlarmList()
    }

    fun fetchAlarmList() {
        intent {
            viewModelScope.launch {
                reduce { state.copy(loading = true) }
                val alarmList = localDatastore.get(LocalDatastore.Keys.alarmList)?.run {
                    if (this.isEmpty()) {
                        emptyList()
                    } else {
                        val result = mutableListOf<Address>()
                        this.forEach {
                            val address = json.decodeFromString<Address>(it)
                            result.add(address)
                        }
                        Log.d(result.toString())
                        result
                    }
                } ?: emptyList()
                reduce { state.copy(alarmList = alarmList, loading = false) }
                postSideEffect("${alarmList.size} data loaded")
            }
        }
    }

    fun getGeocode(address: String) {
        coroutineIOCallbackTo(
            block = { mapsApiUseCase.getGeocode(address) },
            success = { geocode.value = it },
            error = { e -> Log.printStackTrace(e) }
        )
    }

    fun setDataStore(addresses: Addresses?) {
        viewModelScope.launch {
            addresses?.let { addresses ->
                localDatastore.get(LocalDatastore.Keys.alarmList)?.let { addressesSet ->
                    addressesSet.find {
                        val data = json.decodeFromString<Address>(it)
                        data.longitude == addresses.longitude && data.latitude == addresses.latitude
                    }?.let {
                        val result = mutableSetOf<String>()
                        val decodeString = json.decodeFromString<com.data.gpsAlarm.dto.Addresses>(it)
                        val data = json.encodeToString(dataMapper(alarmTitle.value, decodeString.mapper()))
                        result.addAll(addressesSet)
                        result.add(data)
                        localDatastore.set(LocalDatastore.Keys.alarmList, result)
                    } ?: run {
                        val data = dataMapper(alarmTitle.value, addresses)
                        val result = mutableSetOf<String>()
                        result.addAll(addressesSet)
                        result.add(json.encodeToString(data))
                        localDatastore.set(LocalDatastore.Keys.alarmList, result)
                    }
                } ?: run {
                    val data = dataMapper(alarmTitle.value, addresses)
                    val result = mutableSetOf<String>()
                    result.add(json.encodeToString(data))
                    localDatastore.set(LocalDatastore.Keys.alarmList, result)
                }
            }
            this@AlarmVM.chooseAddress.value = null
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
                val callback = object : FlowCallback<Set<String>> {
                    override suspend fun onSuccess(responseData: Set<String>?) {
                        if (responseData.isNullOrEmpty()) {
                            trySend("")
                        } else {
                            responseData.find {
                                val data = json.decodeFromString<Address>(it)
                                data.longitude == longitude.toDouble() && data.latitude == latitude.toDouble()
                            }?.let {
                                trySend(it)
                            }
                        }
                    }

                    override fun onFailure(e: Throwable?) {
                        Log.printStackTrace(e)
                        close(e)
                    }
                }
                localDatastore.get(LocalDatastore.Keys.alarmList)?.let { addressSet ->
                    callback.onSuccess(addressSet)
                } ?: run {
                    callback.onSuccess(null)
                }

                awaitClose {
                    Log.d("getAddress callbackFlow close...")
                    close()
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

    fun deleteAlarm(longitude: Double?, latitude: Double?) {
        viewModelScope.launch {
            callbackFlow {
                val callback = object : FlowCallback<Set<String>> {
                    override suspend fun onSuccess(responseData: Set<String>?) {
                        val result = mutableListOf<String>()

                        if (responseData.isNullOrEmpty()) {
                            trySend(emptyList())
                        } else {
                            responseData.forEach {
                                val data = json.decodeFromString<Address>(it)

                                if (data.longitude == longitude && data.latitude == latitude) {
                                    return@forEach
                                } else {
                                    result.add(it)
                                }
                            }
                            trySend(result)
                        }
                    }

                    override fun onFailure(e: Throwable?) {
                        Log.printStackTrace(e)
                        close(e)
                    }
                }
                localDatastore.get(LocalDatastore.Keys.alarmList)?.let { addressSet ->
                    callback.onSuccess(addressSet)
                }

                awaitClose {
                    Log.d("getAddress callbackFlow close...")
                    close()
                }
            }.onEmpty {
                Log.d("deleteAlarm flow onEmpty")
            }.catch {
                Log.printStackTrace(it)
            }.collect {
                localDatastore.set(LocalDatastore.Keys.alarmList, it.toSet())
                fetchAlarmList()
            }
        }
    }
}