package com.luthfi.ecommerce.ui.main.store

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.luthfi.ecommerce.core.data.datasource.api.body.ProductsBody
import com.luthfi.ecommerce.core.domain.repository.Repository
import kotlinx.coroutines.runBlocking

class StoreViewModel(private val repository: Repository) : ViewModel() {

    var isGridLayout = false
    val productsBody = MutableLiveData(
        ProductsBody(
            null,
            null,
            null,
            null,
            null,
            10,
            1
        )
    )

    val products = productsBody.switchMap { query ->
        repository.product(query).cachedIn(viewModelScope).asLiveData()
    }

//    @OptIn(ExperimentalCoroutinesApi::class)
//    fun products() = productsBody.asFlow().flatMapLatest { query ->
//        repository.product(query).cachedIn(viewModelScope)
//    }

    fun updateFilter(sort: String?, category: String?, lowest: Int?, highest: Int?) {
        runBlocking {
            productsBody.postValue(
                productsBody.value?.copy(
                    sort = sort,
                    brand = category,
                    lowest = lowest,
                    highest = highest
                )
            )
        }
    }

    fun updateSearch(search: String?) {
        runBlocking { productsBody.postValue(productsBody.value?.copy(search = search)) }
    }
}
