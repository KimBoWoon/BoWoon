package com.data.gpsAlarm.repository

import com.data.gpsAlarm.mapper.mapper
import com.data.gpsAlarm.service.MapsApiService
import com.domain.gpsAlarm.dto.Geocode
import com.domain.gpsAlarm.repository.MapsApiRepository

class MapsApiRepositoryImpl(
    private val mapsApiService: MapsApiService
) : MapsApiRepository {
    override suspend fun getGeocode(address: String): Geocode = mapsApiService.getGeocode(address).mapper()
}