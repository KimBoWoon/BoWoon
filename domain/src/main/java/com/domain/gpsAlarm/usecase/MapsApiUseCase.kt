package com.domain.gpsAlarm.usecase

import com.domain.gpsAlarm.dto.Geocode
import com.domain.gpsAlarm.repository.MapsApiRepository

class MapsApiUseCase(
    private val mapsApiRepository: MapsApiRepository
) {
    suspend fun getGeocode(address: String): Geocode = mapsApiRepository.getGeocode(address)
}