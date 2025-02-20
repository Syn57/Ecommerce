package com.luthfi.ecommerce.core.data.datasource.api.responses

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductsResponse(
    val code: Int,
    val data: Data,
    val message: String
) : Parcelable {
    @Parcelize
    data class Data(
        val currentItemCount: Int,
        val items: List<Item>,
        val itemsPerPage: Int,
        val pageIndex: Int,
        val totalPages: Int
    ) : Parcelable {
        @Parcelize
        data class Item(
            val brand: String,
            val image: String,
            val productId: String,
            val productName: String,
            val productPrice: Int,
            val productRating: Double,
            val sale: Int,
            val store: String
        ) : Parcelable
    }
}
