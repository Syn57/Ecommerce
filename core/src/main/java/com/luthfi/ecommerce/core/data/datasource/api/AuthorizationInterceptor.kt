package com.luthfi.ecommerce.core.data.datasource.api

import com.luthfi.ecommerce.core.BuildConfig
import com.luthfi.ecommerce.core.data.datasource.preferences.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor(private val userPreferences: UserPreferences) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        return if (chain.request().url.encodedPath in PRELOGIN_URL_PATH) {
            request = runBlocking {
                request.newBuilder().addHeader(API_KEY, BuildConfig.API_KEY).build()
            }
            chain.proceed(request)
        } else {
            request = runBlocking {
                request.newBuilder().addHeader(
                    AUTHORIZATION, "Bearer ${userPreferences.getAccessToken().first()}"
                ).build()
            }
            chain.proceed(request)
        }
    }

    companion object {
        private val PRELOGIN_URL_PATH = listOf("/register", "/login", "/refresh")
        private const val API_KEY = "API_KEY"
        private const val AUTHORIZATION = "Authorization"
    }
}
