package com.luthfi.ecommerce.core.data.datasource.api

sealed class Resource<out T> {
    data object Loading : Resource<Nothing>()
    data class Error(val error: String) : Resource<Nothing>()
    data class Success<T>(val data: T) : Resource<T>()
}
