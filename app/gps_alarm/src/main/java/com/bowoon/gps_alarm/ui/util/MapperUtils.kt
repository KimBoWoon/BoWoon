package com.bowoon.gps_alarm.ui.util

import com.bowoon.gps_alarm.data.Address
import com.bowoon.gps_alarm.data.Addresses
import com.bowoon.gps_alarm.data.Week
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

inline fun <reified T> String.decode(json: Json): T =
    json.decodeFromString(this)

inline fun <reified T> T.encode(json: Json): String =
    json.encodeToString(this)

fun dataMapper(
    alarmTitle: String,
    weekList: List<Week>,
    addresses: Addresses
): Address = Address(
    alarmTitle,
    isEnable = false,
    addresses.distance,
    addresses.englishAddress,
    addresses.jibunAddress,
    addresses.roadAddress,
    addresses.longitude,
    addresses.latitude,
    weekList = weekList
)