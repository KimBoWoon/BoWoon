package com.gps_alarm.ui.util

import com.domain.gpsAlarm.dto.Addresses
import com.gps_alarm.data.Address

fun dataMapper(alarmTitle: String, addresses: Addresses): Address =
    Address(
        alarmTitle,
        false,
        addresses.distance,
        addresses.englishAddress,
        addresses.jibunAddress,
        addresses.roadAddress,
        addresses.longitude,
        addresses.latitude
    )