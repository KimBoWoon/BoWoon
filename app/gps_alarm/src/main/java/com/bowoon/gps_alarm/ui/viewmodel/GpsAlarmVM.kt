package com.bowoon.gps_alarm.ui.viewmodel

import com.bowoon.gps_alarm.base.BaseVM
import com.bowoon.gps_alarm.data.Address
import com.bowoon.gps_alarm.data.SettingInfo
import com.bowoon.gps_alarm.data.StartServiceData
import com.bowoon.gps_alarm.ui.util.decode
import com.data.gpsAlarm.local.LocalDataStore
import com.domain.gpsAlarm.usecase.DataStoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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
) : BaseVM(), ContainerHost<StartServiceData, GpsAlarmVM.GpsAlarmSideEffect> {
    override val container: Container<StartServiceData, GpsAlarmSideEffect> = container(StartServiceData())

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
            val setting = dataStoreUseCase.get(LocalDataStore.Keys.setting)?.decode(json) ?: SettingInfo()
            reduce { state.copy(alarmList = addressList, settingInfo = setting, loading = false, error = null) }
        }
    }
}