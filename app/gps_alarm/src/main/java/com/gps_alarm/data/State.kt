package com.gps_alarm.data

sealed class State {
    sealed class Permission {
        object Unknown : Permission()
        object Granted : Permission()
        object Denied : Permission()
    }

    sealed class AlarmData {
        object Loading : AlarmData()
        class Success(data: List<Address>) : AlarmData()
        class Error(error: Throwable) : AlarmData()
    }
}

data class AlarmData(
    val alarmList: List<Address> = emptyList(),
    val loading: Boolean = false,
    val error: Throwable? = null
)