package com.data.util

sealed class DataStatus<out R> {
    data object Loading : DataStatus<Nothing>()
    data class Success<T>(val data: T) : DataStatus<T>()
    data class Failure(val throwable: Throwable) : DataStatus<Nothing>()
}