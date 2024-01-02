package com.bowoon.gps_alarm.apis

import com.bowoon.gps_alarm.data.Geocode
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface MapsApiService {
    @GET
    suspend fun getGeocode(
        @Url url: String,
        @Query("query") address: String
    ): Geocode
}