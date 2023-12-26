package com.bowoon.gps_alarm.ui.viewmodel

import com.bowoon.gps_alarm.base.BaseVM
import com.domain.gpsAlarm.usecase.DataStoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class GpsAlarmVM @Inject constructor(
    private val json: Json,
    private val dataStoreUseCase: DataStoreUseCase
) : BaseVM() {
//    override val container: Container<StartServiceData, GpsAlarmSideEffect> = container(StartServiceData())

    sealed class GpsAlarmSideEffect {
        data object CheckPermission : GpsAlarmSideEffect()
    }

    init {
//        checkPermission()
//        getAddressList()
    }

//    private fun checkPermission() {
//        intent {
//            postSideEffect(GpsAlarmSideEffect.CheckPermission)
//        }
//    }
//
//    private fun getAddressList() {
//        intent {
//            reduce { state.copy(alarmList = null, loading = true, error = null) }
//            val addressList = dataStoreUseCase.get(LocalDataStore.Keys.alarmList)?.map { it.decode<Address>(json) } ?: emptyList()
//            val setting = dataStoreUseCase.get(LocalDataStore.Keys.setting)?.decode(json) ?: SettingInfo()
//            reduce { state.copy(alarmList = addressList, settingInfo = setting, loading = false, error = null) }
//        }
//    }
}