package com.bowoon.network

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import javax.inject.Inject

class CustomCallAdapter @Inject constructor() : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java) return null
        check(returnType is ParameterizedType) {
            "Return 타입은 ApiResponse<Foo> 또는 ApiResponse<out Foo>로 정의되어야 합니다."
        }

        val wrapperType = getParameterUpperBound(0, returnType)
        if (getRawType(wrapperType) != ApiResponse::class.java) return null
        check(wrapperType is ParameterizedType) {
            "Return 타입은 ApiResponse<ResponseBody>로 정의되어야 합니다."
        }

        val bodyType = getParameterUpperBound(0, wrapperType)
        return ApiResultCallAdapter<Any>(bodyType)
    }
}