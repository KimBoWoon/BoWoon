package com.gps_alarm.data

import com.naver.maps.map.MapView
import kotlinx.serialization.Serializable

data class AlarmData(
    val alarmList: List<Address>? = null,
    val loading: Boolean = false,
    val error: Throwable? = null
)

data class MapData(
    val mapView: MapView? = null,
    val alarmList: List<Address>? = null,
    val loading: Boolean = false,
    val error: Throwable? = null
)

@Serializable
data class SettingInfo(
    val isFollowing: Boolean = false
)