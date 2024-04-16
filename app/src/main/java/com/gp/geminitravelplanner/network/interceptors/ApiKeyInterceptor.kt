package com.gp.geminitravelplanner.network.interceptors

import com.gp.geminitravelplanner.BuildConfig
import com.gp.geminitravelplanner.di.util.NetworkConstants
import okhttp3.Interceptor
import okhttp3.Response

/*
    OkHttp Interceptor to add GeoApify API key to all requests
*/

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader(NetworkConstants.API_KEY_PARAM, BuildConfig.geoApifyAPIKey)
            .build()
        return chain.proceed(request)
    }
}