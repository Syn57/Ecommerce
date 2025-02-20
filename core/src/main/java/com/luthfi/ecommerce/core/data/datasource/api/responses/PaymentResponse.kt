package com.luthfi.ecommerce.core.data.datasource.api.responses

import com.luthfi.ecommerce.core.domain.model.Payment

data class PaymentResponse(
    val code: Int,
    val data: List<Data>,
    val message: String
) {
    data class Data(
        val item: List<Item>,
        val title: String
    ) {
        data class Item(
            val image: String,
            val label: String,
            val status: Boolean?
        )
    }
}

fun List<PaymentResponse.Data>.toPayment(): List<Payment> {
    val listPayment = mutableListOf<Payment>()
    this.forEach {
        listPayment.add(
            Payment(
                item = it.item.toPaymentItem(),
                title = it.title

            )
        )
    }
    return listPayment
}

fun List<PaymentResponse.Data.Item>.toPaymentItem(): List<Payment.Item> {
    val listItem = mutableListOf<Payment.Item>()
    this.forEach {
        listItem.add(
            Payment.Item(
                image = it.image,
                label = it.label,
                status = it.status
            )
        )
    }
    return listItem
}
