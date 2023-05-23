package com.data.practice.service

import okhttp3.Interceptor
import okhttp3.Response

class AppInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
        request().newBuilder().build().run {
            proceed(this)
        }
    }
}