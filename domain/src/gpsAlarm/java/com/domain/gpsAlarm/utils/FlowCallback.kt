package com.domain.gpsAlarm.utils

interface FlowCallback<T> {
    suspend fun onSuccess(responseData: T?)
    fun onFailure(e: Throwable?)
}