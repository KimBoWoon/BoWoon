package com.data.lol.util

sealed class DataStatus {
    object Loading : DataStatus()
    data class Success<T>(val data: T) : DataStatus()
    data class Failure(val throwable: Throwable) : DataStatus()
}