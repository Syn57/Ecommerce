package com.luthfi.ecommerce.core.data.datasource.api.responses

data class ProfileResponse(
    val code: Int? = null,
    val data: Data? = null,
    val message: String? = null
) {
    data class Data(
        val userImage: String? = null,
        val userName: String? = null
    )
}
