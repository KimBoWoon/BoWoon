package com.bowoon.gps_alarm.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.bowoon.gpsAlarm.BuildConfig
import com.bowoon.gps_alarm.base.BaseVM
import com.bowoon.gps_alarm.data.Address
import com.bowoon.gps_alarm.ui.util.dataMapper
import com.bowoon.gps_alarm.ui.util.decode
import com.bowoon.gps_alarm.ui.util.encode
import com.data.gpsAlarm.local.LocalDataStore
import com.data.gpsAlarm.mapper.mapper
import com.data.util.DataStatus
import com.data.util.Log
import com.domain.gpsAlarm.dto.Addresses
import com.domain.gpsAlarm.dto.Geocode
import com.domain.gpsAlarm.usecase.DataStoreUseCase
import com.domain.gpsAlarm.usecase.MapsApiUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class AlarmVM @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val mapsApiUseCase: MapsApiUseCase,
    private val dataStoreUseCase: DataStoreUseCase,
    private val json: Json
) : BaseVM() {
    companion object {
        private const val TAG = "#AlarmVM"
    }

    val findAddress = MutableStateFlow<Address?>(null)
    val geocode = MutableStateFlow<DataStatus<Geocode>>(DataStatus.Loading)
    val chooseAddress = MutableStateFlow<Addresses?>(null)
    val alarmTitle = MutableStateFlow<String>("")
    val expandedAddressItem = MutableStateFlow(-1)
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
            val data = dataMapper(title, null, addresses)
            Log.d(TAG, data.encode(json))
            setDataStore(data)
        }
    }

    fun fetchAlarmList() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreUseCase.getData(LocalDataStore.Keys.alarmList)
                .onStart { alarmList.value = DataStatus.Loading }
                .catch { e -> alarmList.value = DataStatus.Failure(e) }
                .onEach {
                    it?.let { alarmSet ->
                        if (alarmSet.isEmpty()) {
                            alarmList.value = DataStatus.Success(emptyList())
                        } else {
                            alarmList.value = DataStatus.Success(alarmSet.map { item -> item.decode(json) })
                        }
                    } ?: run {
                        alarmList.value = DataStatus.Success(emptyList())
                    }
                }.launchIn(viewModelScope)
        }
    }

    suspend fun removeAlarm(longitude: Double?, latitude: Double?) {
        if (longitude == null || latitude == null) {
            Log.d(TAG, "정상적인 데이터가 아닙니다!")
        } else {
            dataStoreUseCase.get(LocalDataStore.Keys.alarmList)?.let { alarmList ->
                val noData = alarmList.map { alarm ->
                    alarm.decode<Address>(json)
                }.none {
                    it.longitude == longitude && it.latitude == latitude
                }

                if (noData) {
                    Log.d(TAG, "데이터가 존재하지 않습니다!")
                } else {
                    alarmList.filter { alarm ->
                        alarm.decode<Address>(json).run {
                            this.longitude != longitude && this.latitude != latitude
                        }
                    }.map {
                        it.decode<Address>(json)
                    }.also { list ->
                        list.map {
                            it.encode(json)
                        }.toSet().also { saveData ->
                            dataStoreUseCase.set(LocalDataStore.Keys.alarmList, saveData)
                        }
                    }
                    Log.d(TAG, "데이터를 제거했습니다.")
                }
            } ?: run {
                Log.d(TAG, "저장된 데이터가 없습니다!")
            }
        }
    }

    fun addAlarm(title: String, addresses: Addresses?) {
        viewModelScope.launch(Dispatchers.IO) {
            addresses?.let {
                dataMapper(title, week.toList(), addresses).run {
                    setDataStore(this)
                }
            } ?: run {
                Log.d(TAG, "데이터를 확인해주세요!")
            }
        }
    }

//    fun modifyAddress(address: Address) {
//        intent {
//            postSideEffect(AlarmSideEffect.ModifyAddress(address))
//        }
//    }
//
//    fun goToDetail(longitude: String, latitude: String) {
//        intent {
//            postSideEffect(AlarmSideEffect.GoToDetail(longitude, latitude))
//        }
//    }

    fun getGeocode(address: String) {
        flow {
            emit(mapsApiUseCase.getGeocode(address))
        }.onStart {
            geocode.value = DataStatus.Loading
        }.catch {e ->
            geocode.value = DataStatus.Failure(e)
        }.onEach {
            geocode.value = DataStatus.Success(it)
        }.launchIn(viewModelScope)
    }

    private fun setDataStore(address: Address) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreUseCase.get(LocalDataStore.Keys.alarmList)?.let { addressesSet ->
                addressesSet.find {
                    it.decode<Address>(json).run {
                        longitude == address.longitude && latitude == address.latitude
                    }
                }?.let {
                    mutableSetOf<String>().apply {
                        val decodeString = it.decode<com.data.gpsAlarm.dto.Addresses>(json)
                        val data = dataMapper(alarmTitle.value, address.weekList ?: emptyList(), decodeString.mapper()).encode(json)
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

//    fun getAddress(longitude: String, latitude: String) {
//        intent {
//            val address = dataStoreUseCase.get(LocalDataStore.Keys.alarmList)
//
//            if (address.isNullOrEmpty()) {
//                postSideEffect(AlarmSideEffect.ShowToast("데이터가 없습니다!"))
//            } else {
//                address.find {
//                    it.decode<Address>(json).run {
//                        longitude == this.longitude.toString() && latitude == this.latitude.toString()
//                    }
//                }?.let {
//                    postSideEffect(AlarmSideEffect.GetAddress(it.decode(json)))
//                } ?: run {
//                    postSideEffect(AlarmSideEffect.GetAddress(null))
//                }
//            }
//        }
//    }
}