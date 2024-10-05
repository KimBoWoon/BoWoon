package com.bowoon.network

sealed class ApiResponse<out R> {
    data class Success<T>(val data: T) : ApiResponse<T>()
    data class Failure(
        val code: Int? = null,
        val message: String? = null,
        val body: String? = null,
        val throwable: Throwable? = null
    ) : ApiResponse<Nothing>()

    fun isSuccess(): Boolean = this is Success

    fun isFailure(): Boolean = this is Failure

    fun getOrThrow(): R {
        throwOnFailure()
        return (this as Success).data
    }

    fun getOrNull(): R? =
        when (this) {
            is Success -> data
            else -> null
        }

    fun failureOrThrow(): Failure {
        throwOnSuccess()
        return this as Failure
    }

    fun exceptionOrNull(): Throwable? =
        when (this) {
            is Failure -> safeThrowable()
            else -> null
        }

    fun safeThrowable(): Throwable = Throwable("Something wrong...")

//    fun safeThrowable(): Throwable = when (this) {
//        is HttpError -> IllegalStateException("$message $body")
//        is NetworkError -> throwable
//        is UnknownApiError -> throwable
//    }
}

internal fun ApiResponse<*>.throwOnFailure() {
    if (this is ApiResponse.Failure) throw safeThrowable()
}

internal fun ApiResponse<*>.throwOnSuccess() {
    if (this is ApiResponse.Success) throw IllegalStateException("Cannot be called under Success conditions.")
}

inline fun <T> ApiResponse<T>.onSuccess(
    action: (value: T) -> Unit
): ApiResponse<T> {
    if (isSuccess()) action(getOrThrow())
    return this
}

inline fun <T> ApiResponse<T>.onFailure(
    action: (error: ApiResponse.Failure) -> Unit
): ApiResponse<T> {
    if (isFailure()) action(failureOrThrow())
    return this
}