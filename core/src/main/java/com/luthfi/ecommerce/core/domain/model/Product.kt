package com.luthfi.ecommerce.core.domain.model

import android.os.Parcelable
import com.luthfi.ecommerce.core.data.datasource.room.entity.ChartEntity
import com.luthfi.ecommerce.core.data.datasource.room.entity.FavoriteEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val productId: String,
    val brand: String,
    val description: String,
    val image: String,
    val productName: String,
    val productPrice: Int,
    val productRating: Double,
    var variantName: String,
    var variantPrice: Int,
    val sale: Int,
    val stock: Int,
    val store: String,
    val totalRating: Int,
    val totalReview: Int,
    val totalSatisfaction: Int,
    var isChecked: Boolean = false,
    var count: Int = 0
) : Parcelable

fun Product.toChartEntity(count: Int, isChecked: Boolean): ChartEntity {
    return ChartEntity(
        productId = this.productId,
        brand = this.brand,
        description = this.description,
        image = this.image,
        productName = this.productName,
        productPrice = this.productPrice,
        productRating = this.productRating,
        variantName = this.variantName,
        variantPrice = this.variantPrice,
        sale = this.sale,
        stock = this.stock,
        store = this.store,
        totalRating = this.totalRating,
        totalReview = this.totalReview,
        totalSatisfaction = this.totalSatisfaction,
        isChecked = isChecked,
        count = count
    )
}

fun Product.toFavEntity(): FavoriteEntity {
    return FavoriteEntity(
        productId = this.productId,
        brand = this.brand,
        description = this.description,
        image = this.image,
        productName = this.productName,
        productPrice = this.productPrice,
        productRating = this.productRating,
        variantName = this.variantName,
        variantPrice = this.variantPrice,
        sale = this.sale,
        stock = this.stock,
        store = this.store,
        totalRating = this.totalRating,
        totalReview = this.totalReview,
        totalSatisfaction = this.totalSatisfaction
    )
}
