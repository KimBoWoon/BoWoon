package com.gps_alarm.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.data.gpsAlarm.local.LocalDataStore
import com.domain.gpsAlarm.usecase.DataStoreUseCase
import com.gps_alarm.base.BaseVM
import com.gps_alarm.data.Address
import com.gps_alarm.ui.util.decode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.serialization.json.Json
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class GpsAlarmVM @Inject constructor(
    private val json: Json,
    private val dataStoreUseCase: DataStoreUseCase
) : BaseVM(), ContainerHost<List<Address>, GpsAlarmVM.GpsAlarmSideEffect> {
    var appBarTitle = mutableStateOf("GPS 알람")
    override val container: Container<List<Address>, GpsAlarmSideEffect> = container(emptyList())

    sealed class GpsAlarmSideEffect {
        object CheckPermission : GpsAlarmSideEffect()
    }

    init {
        checkPermission()
    }

    private fun checkPermission() {
        intent {
            postSideEffect(GpsAlarmSideEffect.CheckPermission)
        }
    }

    suspend fun getAddressList(): Array<Address> =
        viewModelScope.async {
            dataStoreUseCase.get(LocalDataStore.Keys.alarmList)?.map { it.decode<Address>(json) }?.toTypedArray()
        }.await() ?: emptyArray()
}