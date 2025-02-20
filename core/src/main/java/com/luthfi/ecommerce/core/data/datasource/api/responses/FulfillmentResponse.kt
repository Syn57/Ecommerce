package com.luthfi.ecommerce.core.data.datasource.api.responses

data class FulfillmentResponse(
    val code: Int?,
    val data: Data?,
    val message: String?
) {
    data class Data(
        val date: String?,
        val invoiceId: String?,
        val payment: String?,
        val status: Boolean?,
        val time: String?,
        val total: Int?
    )
}
