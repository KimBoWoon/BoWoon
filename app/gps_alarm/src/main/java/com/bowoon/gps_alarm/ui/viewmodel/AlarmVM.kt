package com.bowoon.gps_alarm.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.bowoon.gpsAlarm.BuildConfig
import com.bowoon.gps_alarm.base.BaseVM
import com.bowoon.gps_alarm.data.Address
import com.bowoon.gps_alarm.data.AlarmData
import com.bowoon.gps_alarm.ui.util.dataMapper
import com.bowoon.gps_alarm.ui.util.decode
import com.bowoon.gps_alarm.ui.util.encode
import com.data.gpsAlarm.local.LocalDataStore
import com.data.gpsAlarm.mapper.mapper
import com.data.util.Log
import com.domain.gpsAlarm.dto.Addresses
import com.domain.gpsAlarm.dto.Geocode
import com.domain.gpsAlarm.usecase.DataStoreUseCase
import com.domain.gpsAlarm.usecase.MapsApiUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class AlarmVM @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val mapsApiUseCase: MapsApiUseCase,
    private val dataStoreUseCase: DataStoreUseCase,
    private val json: Json
) : ContainerHost<AlarmData, AlarmVM.AlarmSideEffect>, BaseVM() {
    val findAddress = MutableStateFlow<Address?>(null)
    val geocode = MutableStateFlow<Geocode?>(null)
    val chooseAddress = MutableStateFlow<Addresses?>(null)
    val alarmTitle = MutableStateFlow<String>("")
    val expandedAddressItem = MutableStateFlow(-1)
    val week = mutableSetOf<String>()

    override val container: Container<AlarmData, AlarmSideEffect> = container(AlarmData(alarmList = null, loading = true, error = null))

    sealed class AlarmSideEffect {
        data class ShowToast(val message: String) : AlarmSideEffect()
        data class SaveListToDataStore(val data: List<Address>) : AlarmSideEffect()
        data class AddAlarm(val data: Address) : AlarmSideEffect()
        data class ModifyAddress(val data: Address) : AlarmSideEffect()
        data class GoToDetail(val longitude: String, val latitude: String) : AlarmSideEffect()
        data class GetGeocode(val geocode: Geocode) : AlarmSideEffect()
        data class GetAddress(val address: Address?) : AlarmSideEffect()
    }

//    init {
//        if (BuildConfig.DEBUG) {
//            addAlarm(
//                "스타벅스 석촌호수점",
//                Addresses(
//                    null,
//                    0.0,
//                    null,
//                    "서울 송파구 송파동 7-4",
//                    "서울 송파구 석촌호수로 262",
//                    127.10533937925041,
//                    37.50952059479555
//                )
//            )
//        }
//    }

    fun fetchAlarmList() {
        intent {
            reduce { state.copy(alarmList = null, loading = true, error = null) }
            dataStoreUseCase.get(LocalDataStore.Keys.alarmList)?.let { alarmList ->
                reduce {
                    state.copy(
                        alarmList = if (alarmList.isEmpty()) {
                            emptyList()
                        } else {
                            alarmList.map { it.decode(json) }
                        },
                        loading = false,
                        error = null
                    )
                }
            } ?: run {
                reduce { state.copy(alarmList = emptyList(), loading = false, error = null) }
            }
        }
    }

    fun removeAlarm(longitude: Double?, latitude: Double?) {
        intent {
            if (longitude == null || latitude == null) {
                postSideEffect(AlarmSideEffect.ShowToast("정상적인 데이터가 아닙니다!"))
            } else {
                reduce { state.copy(loading = true) }
                dataStoreUseCase.get(LocalDataStore.Keys.alarmList)?.let { alarmList ->
                    val noData = alarmList.map { alarm ->
                        alarm.decode<Address>(json)
                    }.none {
                        it.longitude == longitude && it.latitude == latitude
                    }

                    if (noData) {
                        postSideEffect(AlarmSideEffect.ShowToast("데이터가 존재하지 않습니다!"))
                    } else {
                        val list = alarmList.filter { alarm ->
                            alarm.decode<Address>(json).run {
                                this.longitude != longitude && this.latitude != latitude
                            }
                        }.map { it.decode<Address>(json) }
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
                val address = dataMapper(title, week.toList(), addresses)
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
            it.encode(json)
        }.toSet()
        dataStoreUseCase.set(LocalDataStore.Keys.alarmList, saveData)
    }

    fun getGeocode(address: String) {
        intent {
            runCatching {
                mapsApiUseCase.getGeocode(address)
            }.onSuccess {
                postSideEffect(AlarmSideEffect.GetGeocode(it))
            }.onFailure { e ->
                Log.printStackTrace(e)
                postSideEffect(AlarmSideEffect.ShowToast("주소를 찾지 못 했습니다."))
            }
        }
    }

    fun setDataStore(address: Address) {
        viewModelScope.launch {
            dataStoreUseCase.get(LocalDataStore.Keys.alarmList)?.let { addressesSet ->
                addressesSet.find {
                    it.decode<Address>(json).run {
                        longitude == address.longitude && latitude == address.latitude
                    }
                }?.let {
                    mutableSetOf<String>().apply {
                        val decodeString = it.decode<com.data.gpsAlarm.dto.Addresses>(json)
                        val data = dataMapper(alarmTitle.value, address.weekList, decodeString.mapper()).encode(json)
                        addAll(addressesSet)
                        add(data)
                        dataStoreUseCase.set(LocalDataStore.Keys.alarmList, this)
                    }
                } ?: run {
                    mutableSetOf<String>().apply {
                        addAll(addressesSet)
                        add(address.encode(json))
                        dataStoreUseCase.set(LocalDataStore.Keys.alarmList, this)
                    }
                }
            } ?: run {
                mutableSetOf<String>().apply {
                    add(address.encode(json))
                    dataStoreUseCase.set(LocalDataStore.Keys.alarmList, this)
                }
            }
            this@AlarmVM.chooseAddress.value = null
        }
    }

    fun changeData(address: Address) {
        viewModelScope.launch {
            dataStoreUseCase.get(LocalDataStore.Keys.alarmList)?.let { addressesSet ->
                addressesSet.find {
                    it.decode<Address>(json).run {
                        longitude == address.longitude && latitude == address.latitude
                    }
                }?.let {
                    mutableSetOf<String>().apply {
                        val encodeString = address.encode(json)
                        addressesSet.forEach {
                            it.decode<Address>(json).apply {
                                add(
                                    if (longitude == address.longitude && latitude == address.latitude) {
                                        encodeString
                                    } else {
                                        it
                                    }
                                )
                            }
                        }
                        dataStoreUseCase.set(LocalDataStore.Keys.alarmList, this)
                    }
                }
            }
        }
    }

    fun getAddress(longitude: String, latitude: String) {
        intent {
            val address = dataStoreUseCase.get(LocalDataStore.Keys.alarmList)

            if (address.isNullOrEmpty()) {
                postSideEffect(AlarmSideEffect.ShowToast("데이터가 없습니다!"))
            } else {
                address.find {
                    it.decode<Address>(json).run {
                        longitude == this.longitude.toString() && latitude == this.latitude.toString()
                    }
                }?.let {
                    postSideEffect(AlarmSideEffect.GetAddress(it.decode(json)))
                } ?: run {
                    postSideEffect(AlarmSideEffect.GetAddress(null))
                }
            }
        }
    }

    fun onCardArrowClicked(index: Int) {
        if (expandedAddressItem.value == -1) {
            expandedAddressItem.value = index
        } else if (expandedAddressItem.value == index) {
            expandedAddressItem.value = -1
        } else if (expandedAddressItem.value > -1) {
            expandedAddressItem.value = -1
            expandedAddressItem.value = index
        }
    }
}