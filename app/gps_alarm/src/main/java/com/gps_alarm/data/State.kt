package com.gps_alarm.data

data class AlarmData(
    val alarmList: List<Address>? = null,
    val loading: Boolean = false,
    val error: Throwable? = null
)