package com.luthfi.ecommerce.core.data.datasource.api.body

data class ProductsBody(
    var search: String?,
    var brand: String?,
    var lowest: Int?,
    var highest: Int?,
    var sort: String?,
    var limit: Int?,
    var page: Int?
)
