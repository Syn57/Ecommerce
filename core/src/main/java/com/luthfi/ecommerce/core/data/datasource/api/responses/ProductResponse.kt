package com.luthfi.ecommerce.core.data.datasource.api.responses

import com.luthfi.ecommerce.core.domain.model.Product

data class ProductResponse(
    val code: Int,
    val data: Data,
    val message: String
) {
    data class Data(
        val brand: String,
        val description: String,
        val image: List<String>,
        val productId: String,
        val productName: String,
        val productPrice: Int,
        val productRating: Double,
        val productVariant: List<ProductVariant>,
        val sale: Int,
        val stock: Int,
        val store: String,
        val totalRating: Int,
        val totalReview: Int,
        val totalSatisfaction: Int
    ) {
        data class ProductVariant(
            val variantName: String,
            val variantPrice: Int
        )
    }
}

fun ProductResponse.toProduct(): Product {
    return Product(
        productId = this.data.productId,
        brand = this.data.brand,
        description = this.data.description,
        image = this.data.image[0],
        productName = this.data.productName,
        productPrice = this.data.productPrice,
        productRating = this.data.productRating,
        variantName = this.data.productVariant[0].variantName,
        variantPrice = this.data.productVariant[0].variantPrice,
        sale = this.data.sale,
        stock = this.data.stock,
        store = this.data.store,
        totalRating = this.data.totalRating,
        totalReview = this.data.totalReview,
        totalSatisfaction = this.data.totalSatisfaction

    )
}
