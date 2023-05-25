package com.gps_alarm.ui.viewmodel

import com.data.gpsAlarm.local.LocalDatastore
import com.gps_alarm.base.BaseVM
import com.gps_alarm.data.AlarmData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class MapVM @Inject constructor(
    private val localDataStore: LocalDatastore,
    private val json: Json
) : ContainerHost<AlarmData, MapVM.MapSideEffect>, BaseVM() {
    override val container: Container<AlarmData, MapSideEffect> = container(AlarmData())

    sealed class MapSideEffect {

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
                        loading = false)
                }
            }
        }
    }
}