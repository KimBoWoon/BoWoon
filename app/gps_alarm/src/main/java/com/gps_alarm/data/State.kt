package com.gps_alarm.data

data class AlarmData(
    val alarmList: List<Address> = emptyList(),
    val loading: Boolean = false,
    val error: Throwable? = null
)