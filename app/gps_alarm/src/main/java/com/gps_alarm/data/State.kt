package com.gps_alarm.data

import android.os.Parcelable
import com.naver.maps.map.MapView
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

data class AlarmData(
    val alarmList: List<Address>? = null,
    val loading: Boolean = false,
    val error: Throwable? = null
)

data class MapData(
    val mapView: MapView? = null,
    val alarmList: List<Address>? = null,
    val settingInfo: SettingInfo? = null,
    val loading: Boolean = false,
    val error: Throwable? = null
)

@Serializable
@Parcelize
data class SettingInfo(
    val isFollowing: Boolean = false,
    val circleSize: Int = 0
) : Parcelable

data class StartServiceData(
    val alarmList: List<Address>? = null,
    val settingInfo: SettingInfo? = null,
    val loading: Boolean = false,
    val error: Throwable? = null
)