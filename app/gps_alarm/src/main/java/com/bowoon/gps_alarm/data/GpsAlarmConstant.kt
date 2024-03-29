package com.bowoon.gps_alarm.data

object GpsAlarmConstant {
    const val geocodeUrl = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode"

    const val EXPAND_ANIMATION_DURATION = 450
    const val EXPANSION_TRANSITION_DURATION = 450

    const val FIND_ADDRESS_OK = "OK"
    const val FIND_ADDRESS_INVALID_REQUEST = "INVALID_REQUEST"
    const val FIND_ADDRESS_SYSTEM_ERROR = "SYSTEM_ERROR"

    const val SIXTEEN_BY_NINE_RATE = 9f / 16f
}

enum class Week(val label: String) {
    MONDAY("월"),
    TUESDAY("화"),
    WEDNESDAY("수"),
    THURSDAY("목"),
    FRIDAY("금"),
    SATURDAY("토"),
    SUNDAY("일")
}