package com.bowoon.gps_alarm.apis

import com.bowoon.gpsAlarm.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AppInterceptor @Inject constructor(

) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
        proceed(
            request().newBuilder()
                .addHeader("X-NCP-APIGW-API-KEY-ID", BuildConfig.NAVER_MAPS_CLIENT_KEY)
                .addHeader("X-NCP-APIGW-API-KEY", BuildConfig.NAVER_MAPS_CLIENT_SECRET_KEY)
                .build()
        )
    }
}