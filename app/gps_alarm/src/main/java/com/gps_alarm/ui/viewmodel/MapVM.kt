package com.gps_alarm.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.data.gpsAlarm.local.LocalDatastore
import com.domain.gpsAlarm.utils.FlowCallback
import com.gps_alarm.base.BaseVM
import com.gps_alarm.data.Address
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import util.DataStatus
import util.Log
import javax.inject.Inject

@HiltViewModel
class MapVM @Inject constructor(
    private val localDatastore: LocalDatastore,
    private val json: Json
) : BaseVM() {
    val addressList = MutableStateFlow<DataStatus<List<Address>>>(DataStatus.Loading)

    init {
        getAddress()
    }

    fun getAddress() {
        viewModelScope.launch {
            callbackFlow {
                val callback = object : FlowCallback<Set<String>> {
                    override suspend fun onSuccess(responseData: Set<String>?) {
                        if (responseData.isNullOrEmpty()) {
                            trySend(emptyList())
                        } else {
                            val result = responseData.map {
                                val address = json.decodeFromString<Address>(it)
                                Address(
                                    address.name,
                                    address.isEnable,
                                    address.distance,
                                    address.englishAddress,
                                    address.jibunAddress,
                                    address.roadAddress,
                                    address.longitude,
                                    address.latitude
                                )
                            }
                            trySend(result)
                        }
                    }

                    override fun onFailure(e: Throwable?) {
                        Log.printStackTrace(e)
                        close(e)
                    }
                }

                localDatastore.get(LocalDatastore.Keys.alarmList)?.let { addressesSet ->
                    callback.onSuccess(addressesSet)
                } ?: run {
                    callback.onSuccess(emptySet())
                }

                awaitClose {
                    Log.d("MapVM getAddress is close...")
                    close()
                }
            }.onStart {
                addressList.value = DataStatus.Loading
            }.onEmpty {
                addressList.value = DataStatus.Success(emptyList())
            }.catch {
                addressList.value = DataStatus.Failure(it)
            }.collect {
                addressList.value = DataStatus.Success(it)
            }
        }
    }
}