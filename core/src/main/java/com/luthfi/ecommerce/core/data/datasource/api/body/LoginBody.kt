package com.luthfi.ecommerce.core.data.datasource.api.body

data class LoginBody(
    val password: String? = null,
    val firebaseToken: String? = null,
    val email: String? = null
)
