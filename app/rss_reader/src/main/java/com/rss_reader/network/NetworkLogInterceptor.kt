package com.rss_reader.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import com.data.util.Log
import java.io.IOException

/**
 * Network Log Message 출력
 */
class NetworkLogInterceptor : Interceptor {
    private val TAG = javaClass.simpleName

    companion object {
        private var STRING_DIVIDER_CNT = 1000
        private const val NETWORK_LOG_BODY = " \n" +
                "----------------------------------------------------------------------------------\n" +
                "ReqMethod : %s\n" +
                "ReqUrl : %s\n" +
                "ReqBody : %s\n" +
                "ReqHeader : \n%s\n" +
                "----------------------------------------------------------------------------------\n" +
                "ResHeader : %s" +
                "ResBody : %s\n" +
                "----------------------------------------------------------------------------------\n "
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val request = original.newBuilder().build()
        val response = chain.proceed(request)
        val resHeader = response.headers.toString()
        val bodyString = response.body?.string() ?: ""
        var networkLog = String.format(
            NETWORK_LOG_BODY,
            request.method,
            request.url,
            bodyToString(request),
            request.headers.toString(),
            resHeader,
            bodyString
        )

        //Log 중간에 짤리지 않도록 함.
        runCatching {
            while (networkLog.isNotEmpty()) {
                if (networkLog.length > STRING_DIVIDER_CNT) {
                    Log.d("", networkLog.substring(0, STRING_DIVIDER_CNT))
                    networkLog = networkLog.substring(STRING_DIVIDER_CNT)
                } else {
                    Log.d("", networkLog)
                    break
                }
            }
        }.onFailure { e ->
            Log.printStackTrace(e)
        }

        return response.newBuilder().body(bodyString.toResponseBody(response.body?.contentType())).build()
    }

    /**
     * 리스폰스바디의 값 가져오기
     *
     * @param request 가져와야할 리퀘스트
     * @return 바디 값
     */
    private fun bodyToString(request: Request): String? =
        runCatching {
            val copy = request.newBuilder().build()
            val buffer = Buffer()
            copy.body?.writeTo(buffer)
            buffer.readUtf8()
        }.onSuccess {
            return it
        }.onFailure { e ->
            (e as? IOException)?.run {
                return "did not work"
            }
            (e as? NullPointerException)?.run {
                return "did not have body"
            }
        }.getOrNull()
}