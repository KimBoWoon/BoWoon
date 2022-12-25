package com.gps_alarm.javascript

interface GpsAlarmInterface {
    companion object {
        const val INTERFACE_NAME: String = "gpsAlarm"
    }

    fun onComplete(data: String)
}