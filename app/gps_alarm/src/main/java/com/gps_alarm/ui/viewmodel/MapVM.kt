package com.gps_alarm.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.data.gpsAlarm.local.LocalDatastore
import com.gps_alarm.base.BaseVM
import com.gps_alarm.data.Address
import com.gps_alarm.data.AlarmData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import util.Log
import javax.inject.Inject

@HiltViewModel
class MapVM @Inject constructor(
    private val localDatastore: LocalDatastore,
    private val json: Json
) : ContainerHost<AlarmData, MapVM.MapSideEffect>, BaseVM() {
    override val container: Container<AlarmData, MapSideEffect> = container(AlarmData())

    sealed class MapSideEffect {

    }

    init {
        fetchAlarmList()
    }

    fun fetchAlarmList() {
        intent {
            viewModelScope.launch {
                reduce { state.copy(alarmList = emptyList(), loading = true) }
                val alarmList = localDatastore.get(LocalDatastore.Keys.alarmList)?.let { alarmList ->
                    if (alarmList.isEmpty()) {
                        emptyList()
                    } else {
                        mutableListOf<Address>().apply {
                            alarmList.forEach { add(json.decodeFromString(it)) }
                            Log.d(toString())
                        }
                    }
                } ?: emptyList()
                reduce { state.copy(alarmList = alarmList, loading = false) }
            }
        }
    }
}