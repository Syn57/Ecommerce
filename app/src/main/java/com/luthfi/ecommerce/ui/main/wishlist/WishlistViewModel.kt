package com.luthfi.ecommerce.ui.main.wishlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.luthfi.ecommerce.core.domain.model.Product
import com.luthfi.ecommerce.core.domain.repository.Repository
import kotlinx.coroutines.launch

class WishlistViewModel(private val repository: Repository) : ViewModel() {

    var isGridLayout = false

    private var _stock: MutableLiveData<Int?> = MutableLiveData<Int?>()
    val stock: LiveData<Int?> = _stock

    // Database
    fun deleteFav(id: String) {
        viewModelScope.launch {
            repository.deleteItemFav(id)
        }
    }

    suspend fun addChart(product: Product, action: Boolean): String {
        return repository.addChart(product, action)
    }

    fun getAllFav() = repository.getAllFav().asLiveData()
}
