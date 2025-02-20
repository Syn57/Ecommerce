package com.luthfi.ecommerce.core.data.datasource.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.luthfi.ecommerce.core.domain.model.Product

@Entity(tableName = "favorite")
data class FavoriteEntity(
    @PrimaryKey
    val productId: String,
    val brand: String,
    val description: String,
    val image: String,
    val productName: String,
    val productPrice: Int,
    val productRating: Double,
    val variantName: String,
    val variantPrice: Int,
    val sale: Int,
    val stock: Int,
    val store: String,
    val totalRating: Int,
    val totalReview: Int,
    val totalSatisfaction: Int,
)

fun FavoriteEntity.toDomain(): Product {
    return Product(
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

fun List<FavoriteEntity>.toProducts(): List<Product> {
    val listFav = mutableListOf<Product>()
    this.forEach {
        listFav.add(
            Product(
                productId = it.productId,
                brand = it.brand,
                description = it.description,
                image = it.image,
                productName = it.productName,
                productPrice = it.productPrice,
                productRating = it.productRating,
                variantName = it.variantName,
                variantPrice = it.variantPrice,
                sale = it.sale,
                stock = it.stock,
                store = it.store,
                totalRating = it.totalRating,
                totalReview = it.totalReview,
                totalSatisfaction = it.totalSatisfaction
            )
        )
    }
    return listFav
}
