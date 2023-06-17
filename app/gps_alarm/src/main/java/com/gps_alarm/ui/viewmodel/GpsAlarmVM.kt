package com.gps_alarm.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.data.gpsAlarm.local.LocalDataStore
import com.domain.gpsAlarm.usecase.DataStoreUseCase
import com.gps_alarm.base.BaseVM
import com.gps_alarm.data.Address
import com.gps_alarm.data.AlarmData
import com.gps_alarm.ui.util.decode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.serialization.json.Json
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class GpsAlarmVM @Inject constructor(
    private val json: Json,
    private val dataStoreUseCase: DataStoreUseCase
) : BaseVM(), ContainerHost<AlarmData, GpsAlarmVM.GpsAlarmSideEffect> {
    var appBarTitle = mutableStateOf("GPS 알람")
    override val container: Container<AlarmData, GpsAlarmSideEffect> = container(AlarmData())

    sealed class GpsAlarmSideEffect {
        object CheckPermission : GpsAlarmSideEffect()
    }

    init {
        checkPermission()
        getAddressList()
    }

    private fun checkPermission() {
        intent {
            postSideEffect(GpsAlarmSideEffect.CheckPermission)
        }
    }

    private fun getAddressList() {
        intent {
            reduce { state.copy(alarmList = null, loading = true, error = null) }
            val addressList = dataStoreUseCase.get(LocalDataStore.Keys.alarmList)?.map { it.decode<Address>(json) } ?: emptyList()
            reduce { state.copy(alarmList = addressList, loading = false, error = null) }
        }
    }

//    suspend fun getAddressList(): Array<Address> =
//        viewModelScope.async {
//            dataStoreUseCase.get(LocalDataStore.Keys.alarmList)?.map { it.decode<Address>(json) }?.toTypedArray()
//        }.await() ?: emptyArray()
}