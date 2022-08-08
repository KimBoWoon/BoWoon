package com.data.base.util

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class NetworkLogInterceptor : Interceptor {
    companion object {
        private const val STRING_DIVIDER_CNT = 1000
    }

    val LOG_BODY = "requestHeaders >>>>>\n%s\n" +
            "requestBody >>>>>\n%s\n" +
            "responseHeaders >>>>>\n%s\n" +
            "responseBody >>>>>\n%s"

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().build()
        val response = chain.proceed(request)
        val requestHeaders = request.headers.toString()
        val requestBody = request.body?.toString() ?: ""
        val responseHeaders = response.headers.toString()
        val responseBody = response.body?.string() ?: ""
        var logBody = String.format(LOG_BODY, requestHeaders, requestBody, responseHeaders, responseBody)

        try {
            while (logBody.isNotEmpty()) {
                if (logBody.length > STRING_DIVIDER_CNT) {
                    Log.d("", logBody.substring(0, STRING_DIVIDER_CNT))
                    logBody = logBody.substring(STRING_DIVIDER_CNT)
                } else {
                    Log.d("", logBody)
                    break
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return response.newBuilder().body(responseBody.toResponseBody(response.body?.contentType())).build()
    }
}