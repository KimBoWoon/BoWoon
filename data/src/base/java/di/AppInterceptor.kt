package di

import com.data.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AppInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
        val newRequest = request().newBuilder()
            .addHeader("X-NCP-APIGW-API-KEY-ID", BuildConfig.NAVER_MAPS_CLIENT_KEY)
            .addHeader("X-NCP-APIGW-API-KEY", BuildConfig.NAVER_MAPS_CLIENT_SECRET_KEY)
            .build()
        proceed(newRequest)
    }
}