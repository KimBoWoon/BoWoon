package com.gps_alarm.ui.viewmodel

import com.data.gpsAlarm.local.LocalDatastore
import com.domain.gpsAlarm.usecase.DataStoreUseCase
import com.gps_alarm.base.BaseVM
import com.gps_alarm.data.AlarmData
import com.gps_alarm.ui.util.decode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.serialization.json.Json
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class MapVM @Inject constructor(
    private val dataStoreUseCase: DataStoreUseCase,
    private val json: Json
) : ContainerHost<AlarmData, MapVM.MapSideEffect>, BaseVM() {
    override val container: Container<AlarmData, MapSideEffect> = container(AlarmData())

    sealed class MapSideEffect {

    }

    fun fetchAlarmList() {
        intent {
            reduce { state.copy(alarmList = emptyList(), loading = true) }
            dataStoreUseCase.get(LocalDatastore.Keys.alarmList)?.let { alarmList ->
                reduce {
                    state.copy(
                        alarmList = if (alarmList.isEmpty()) {
                            emptyList()
                        } else {
                            alarmList.map { it.decode(json) }
                        },
                        loading = false)
                }
            }
        }
    }
}