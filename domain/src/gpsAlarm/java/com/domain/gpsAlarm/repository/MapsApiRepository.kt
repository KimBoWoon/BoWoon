package com.domain.gpsAlarm.repository

import com.domain.gpsAlarm.dto.Geocode

interface MapsApiRepository {
    suspend fun getGeocode(address: String): Geocode
}