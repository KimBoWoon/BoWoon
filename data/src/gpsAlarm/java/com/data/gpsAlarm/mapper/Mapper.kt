package com.data.gpsAlarm.mapper

import com.domain.gpsAlarm.dto.AddressElement
import com.domain.gpsAlarm.dto.Addresses
import com.domain.gpsAlarm.dto.Geocode
import com.domain.gpsAlarm.dto.Meta

fun com.data.gpsAlarm.dto.Geocode.mapper(): Geocode =
    Geocode(addresses?.mapper(), errorMessage, meta?.mapper(), status)

@JvmName("addressesMapper")
fun List<com.data.gpsAlarm.dto.Addresses>.mapper(): List<Addresses> =
    this.map { it.mapper() }

fun com.data.gpsAlarm.dto.Addresses.mapper(): Addresses =
    Addresses(addressElements?.mapper(), distance, englishAddress, jibunAddress, roadAddress, longitude, latitude)

@JvmName("addressElementMapper")
fun List<com.data.gpsAlarm.dto.AddressElement>.mapper(): List<AddressElement> =
    this.map { it.mapper() }

fun com.data.gpsAlarm.dto.AddressElement.mapper(): AddressElement =
    AddressElement(code, longName, shortName, types)

fun com.data.gpsAlarm.dto.Meta.mapper(): Meta =
    Meta(count, page, totalCount)