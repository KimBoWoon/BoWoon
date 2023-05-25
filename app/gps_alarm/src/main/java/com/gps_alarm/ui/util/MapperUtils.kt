package com.gps_alarm.ui.util

import com.domain.gpsAlarm.dto.Addresses
import com.gps_alarm.data.Address
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

inline fun <reified T> String.decode(json: Json): T =
    json.decodeFromString(this)

inline fun <reified T> T.encode(json: Json): String =
    json.encodeToString(this)

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