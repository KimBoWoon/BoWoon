package com.gps_alarm.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.data.gpsAlarm.local.LocalDatastore
import com.data.gpsAlarm.mapper.mapper
import com.domain.gpsAlarm.dto.Addresses
import com.domain.gpsAlarm.dto.Geocode
import com.domain.gpsAlarm.usecase.MapsApiUseCase
import com.gps_alarm.base.BaseVM
import com.gps_alarm.data.Address
import com.gps_alarm.data.AlarmData
import com.gps_alarm.ui.util.dataMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
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
    private val localDataStore: LocalDatastore,
    private val json: Json
) : ContainerHost<AlarmData, AlarmVM.AlarmSideEffect>, BaseVM() {
    val findAddress = MutableStateFlow<Address?>(null)
    val geocode = MutableStateFlow<Geocode?>(null)
    val chooseAddress = MutableStateFlow<Addresses?>(null)
    val alarmTitle = MutableStateFlow<String>("")

    override val container: Container<AlarmData, AlarmSideEffect> = container(AlarmData())

    sealed class AlarmSideEffect {
        data class ShowToast(val message: String) : AlarmSideEffect()
        data class SaveListToDataStore(val data: List<Address>) : AlarmSideEffect()
        data class AddAlarm(val data: Address) : AlarmSideEffect()
        data class ModifyAddress(val data: Address) : AlarmSideEffect()
        data class GoToDetail(val longitude: String, val latitude: String) : AlarmSideEffect()
    }

    fun fetchAlarmList() {
        intent {
            reduce { state.copy(alarmList = emptyList(), loading = true) }
            localDataStore.get(LocalDatastore.Keys.alarmList)?.let { alarmList ->
                reduce {
                    state.copy(
                        alarmList = if (alarmList.isEmpty()) {
                            emptyList()
                        } else {
                            alarmList.map { json.decodeFromString(it) }
                        },
                        loading = false
                    )
                }
            }
        }
    }

    fun removeAlarm(longitude: Double?, latitude: Double?) {
        intent {
            if (longitude == null || latitude == null) {
                postSideEffect(AlarmSideEffect.ShowToast("정상적인 데이터가 아닙니다!"))
            } else {
                reduce { state.copy(loading = true) }
                localDataStore.get(LocalDatastore.Keys.alarmList)?.let { alarmList ->
                    val noData = alarmList.map { alarm ->
                        json.decodeFromString<Address>(alarm)
                    }.none {
                        it.longitude == longitude && it.latitude == latitude
                    }

                    if (noData) {
                        postSideEffect(AlarmSideEffect.ShowToast("데이터가 존재하지 않습니다!"))
                    } else {
                        val list = alarmList.filter { alarm ->
                            json.decodeFromString<Address>(alarm).run {
                                this.longitude != longitude && this.latitude != latitude
                            }
                        }.map { json.decodeFromString<Address>(it) }
                        reduce { state.copy(alarmList = list, loading = false) }
                        postSideEffect(AlarmSideEffect.SaveListToDataStore(list))
                        postSideEffect(AlarmSideEffect.ShowToast("데이터를 제거했습니다."))
                    }
                } ?: run {
                    postSideEffect(AlarmSideEffect.ShowToast("저장된 데이터가 없습니다!"))
                }
            }
        }
    }

    fun addAlarm(title: String, addresses: Addresses?) {
        intent {
            addresses?.let {
                val address = dataMapper(title, addresses)
                postSideEffect(AlarmSideEffect.AddAlarm(address))
            } ?: run {
                postSideEffect(AlarmSideEffect.ShowToast("데이터를 확인해주세요!"))
            }
        }
    }

    fun modifyAddress(address: Address) {
        intent {
            postSideEffect(AlarmSideEffect.ModifyAddress(address))
        }
    }

    fun goToDetail(longitude: String, latitude: String) {
        intent {
            postSideEffect(AlarmSideEffect.GoToDetail(longitude, latitude))
        }
    }

    suspend fun saveToDataStore(data: List<Address>) {
        val saveData = data.map {
            json.encodeToString(it)
        }.toSet()
        localDataStore.set(LocalDatastore.Keys.alarmList, saveData)
    }

    fun getGeocode(address: String) {
        coroutineIOCallbackTo(
            block = { mapsApiUseCase.getGeocode(address) },
            success = { geocode.value = it },
            error = { e -> Log.printStackTrace(e) }
        )
    }

    fun setDataStore(address: Address) {
        viewModelScope.launch {
            localDataStore.get(LocalDatastore.Keys.alarmList)?.let { addressesSet ->
                addressesSet.find {
                    json.decodeFromString<Address>(it).run {
                        longitude == address.longitude && latitude == address.latitude
                    }
                }?.let {
                    mutableSetOf<String>().apply {
                        val decodeString = json.decodeFromString<com.data.gpsAlarm.dto.Addresses>(it)
                        val data = json.encodeToString(dataMapper(alarmTitle.value, decodeString.mapper()))
                        addAll(addressesSet)
                        add(data)
                        localDataStore.set(LocalDatastore.Keys.alarmList, this)
                    }
                } ?: run {
                    mutableSetOf<String>().apply {
                        addAll(addressesSet)
                        add(json.encodeToString(address))
                        localDataStore.set(LocalDatastore.Keys.alarmList, this)
                    }
                }
            } ?: run {
                mutableSetOf<String>().apply {
                    add(json.encodeToString(address))
                    localDataStore.set(LocalDatastore.Keys.alarmList, this)
                }
            }
            this@AlarmVM.chooseAddress.value = null
        }
    }

    fun changeData(address: Address) {
        viewModelScope.launch {
            localDataStore.get(LocalDatastore.Keys.alarmList)?.let { addressesSet ->
                addressesSet.find {
                    json.decodeFromString<Address>(it).run {
                        longitude == address.longitude && latitude == address.latitude
                    }
                }?.let {
                    mutableSetOf<String>().apply {
                        val encodeString = json.encodeToString(address)
                        addressesSet.forEach {
                            json.decodeFromString<Address>(it).apply {
                                add(
                                    if (longitude == address.longitude && latitude == address.latitude) {
                                        encodeString
                                    } else {
                                        it
                                    }
                                )
                            }
                        }
                        localDataStore.set(LocalDatastore.Keys.alarmList, this)
                    }
                }
            }
        }
    }

    fun getAddress(longitude: String, latitude: String) {
        viewModelScope.launch {
            runCatching {
                localDataStore.get(LocalDatastore.Keys.alarmList)
            }.onSuccess { alarmList ->
                if (alarmList.isNullOrEmpty()) {
                    findAddress.value = null
                } else {
                    alarmList.find {
                        json.decodeFromString<Address>(it).run {
                            longitude == this.longitude.toString() && latitude == this.latitude.toString()
                        }
                    }?.let {
                        findAddress.value = json.decodeFromString<Address>(it)
                    } ?: run {
                        findAddress.value = null
                    }
                }
            }.onFailure { e ->
                Log.printStackTrace(e)
            }
        }
    }
}