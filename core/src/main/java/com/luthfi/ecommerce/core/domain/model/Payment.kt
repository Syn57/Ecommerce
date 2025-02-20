package com.luthfi.ecommerce.core.domain.model

data class Payment(
    val item: List<Item?>?,
    val title: String?
) {
    data class Item(
        val image: String?,
        val label: String?,
        val status: Boolean?
    )
}
