package com.luthfi.ecommerce.core.data.datasource.api

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.luthfi.ecommerce.core.BuildConfig
import com.luthfi.ecommerce.core.data.datasource.api.body.RefreshBody
import com.luthfi.ecommerce.core.data.datasource.api.responses.RefreshResponse
import com.luthfi.ecommerce.core.data.datasource.preferences.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TokenAuthenticator(
    private val userPreferences: UserPreferences,
    private val authorizationInterceptor: AuthorizationInterceptor,
    private val chuckerInterceptor: ChuckerInterceptor
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        synchronized(this) {
            return runBlocking {
                when (val tokenResponse = getUpdatedToken()) {
                    is Resource.Success -> {
                        userPreferences.setAccessToken(tokenResponse.data.data?.accessToken!!)
                        userPreferences.setRefreshToken(tokenResponse.data.data.refreshToken!!)
                        response.request.newBuilder()
                            .header(
                                AUTHORIZATION,
                                "Bearer ${tokenResponse.data.data.accessToken}"
                            )
                            .build()
                    }

                    is Resource.Error -> {
                        userPreferences.logout()
                        null
                    }

                    else -> null
                }
            }
        }
    }

    private suspend fun getUpdatedToken(): Resource<RefreshResponse> {
        val refreshToken = userPreferences.getRefreshToken().first()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClientRefresh = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authorizationInterceptor)
            .addInterceptor(chuckerInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClientRefresh)
            .build()
        val service = retrofit.create(ApiService::class.java)
        val reqBody = RefreshBody(token = refreshToken)
        val response = service.refreshToken(reqBody)
        return if (response.isSuccessful) {
            Resource.Success(response.body()!!)
        } else {
            Resource.Error(
                ERROR_MESSAGE
            )
        }
    }

    companion object {
        private const val AUTHORIZATION = "Authorization"
        private const val ERROR_MESSAGE = "Token Expired"
    }
}
