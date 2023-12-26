package com.bowoon.gps_alarm.ui.viewmodel

import com.bowoon.gps_alarm.base.BaseVM
import com.domain.gpsAlarm.usecase.DataStoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class MapVM @Inject constructor(
    private val dataStoreUseCase: DataStoreUseCase,
    private val json: Json
) : BaseVM() {
//    override val container: Container<MapData, MapSideEffect> = container(MapData())

    sealed class MapSideEffect {
    }
}