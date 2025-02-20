package com.luthfi.ecommerce.core.data.datasource.api.responses

data class LoginResponse(
    val code: Int? = null,
    val data: Data? = null,
    val message: String? = null
) {
    data class Data(
        val userImage: String? = null,
        val userName: String? = null,
        val accessToken: String? = null,
        val expiresAt: Int? = null,
        val refreshToken: String? = null
    )
}
