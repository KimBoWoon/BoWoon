package com.data.base.util

sealed class DataStatus<out R> {
    object Loading : DataStatus<Nothing>()
    data class Success<T>(val data: T) : DataStatus<T>()
    data class Failure(val throwable: Throwable) : DataStatus<Nothing>()
}