package com.luthfi.ecommerce.core.data.datasource.api.body

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class FulfillmentBody(
    var payment: String?,
    var items: List<Item?>?
) {
    @Parcelize
    data class Item(
        var productId: String?,
        var variantName: String?,
        var quantity: Int?
    ) : Parcelable
}
