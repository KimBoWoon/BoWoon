package com.data.gpsAlarm.service

import com.data.gpsAlarm.dto.Geocode
import retrofit2.http.GET
import retrofit2.http.Query

interface MapsApiService {
    @GET("/map-geocode/v2/geocode")
    suspend fun getGeocode(
        @Query("query") address: String
    ): Geocode
}