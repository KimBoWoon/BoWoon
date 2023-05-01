package com.gps_alarm.data

sealed class Intent {
    sealed class Permission {
        object CheckPermission : Permission()
    }

    sealed class AlarmData {
        object LoadAlarmData : AlarmData()
    }
}