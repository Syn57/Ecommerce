package com.luthfi.ecommerce.core.data.datasource.api.responses

data class TransactionResponse(
    val code: Int?,
    val data: List<Data?>?,
    val message: String?
) {
    data class Data(
        val date: String?,
        val image: String?,
        val invoiceId: String?,
        val items: List<Item?>?,
        val name: String?,
        val payment: String?,
        val rating: Int?,
        val review: String?,
        val status: Boolean?,
        val time: String?,
        val total: Int?
    ) {
        data class Item(
            val productId: String?,
            val quantity: Int?,
            val variantName: String?
        )
    }
}
