package com.bowoon.network

sealed class ApiResponse<out R> {
    data class Success<T>(val data: T) : ApiResponse<T>()
    data class Failure(
        val code: Int? = null,
        val message: String? = null,
        val body: String? = null,
        val throwable: Throwable? = null
    ) : ApiResponse<Nothing>()
}