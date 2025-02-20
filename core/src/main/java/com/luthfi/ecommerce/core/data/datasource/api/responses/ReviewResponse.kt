package com.luthfi.ecommerce.core.data.datasource.api.responses

data class ReviewResponse(
    val code: Int?,
    val data: List<Data?>?,
    val message: String?
) {
    data class Data(
        val userImage: String?,
        val userName: String?,
        val userRating: Int?,
        val userReview: String?
    )
}
