package com.gp.geminitravelplanner.network.interceptors

import com.gp.geminitravelplanner.BuildConfig
import com.gp.geminitravelplanner.di.util.NetworkConstants
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/*
    OkHttp Interceptor to add GeoApify API key to all requests
*/

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url.newBuilder()
            .addQueryParameter(NetworkConstants.API_KEY_PARAM, BuildConfig.geoApifyAPIKey)
            .build()
        val request: Request = chain.request().newBuilder().url(url).build()
        return chain.proceed(request)
    }
}