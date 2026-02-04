package com.zsasko.rawg.common.network

import com.zsasko.rawg.BuildConfig
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class ApiKeyInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Chain): Response {
        val url = chain.request()
            .url
            .newBuilder()
            .addQueryParameter("key", BuildConfig.RAWG_API_KEY)
            .build()

        val request: Request = chain.request()
            .newBuilder()
            .url(url)
            .build()
        val response: Response = chain.proceed(request)
        return response
    }
}