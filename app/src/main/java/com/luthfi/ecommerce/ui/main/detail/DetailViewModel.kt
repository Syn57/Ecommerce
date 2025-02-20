package com.luthfi.ecommerce.ui.main.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.luthfi.ecommerce.core.data.datasource.api.Resource
import com.luthfi.ecommerce.core.data.datasource.api.responses.ProductResponse
import com.luthfi.ecommerce.core.domain.model.Product
import com.luthfi.ecommerce.core.domain.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: Repository, private val state: SavedStateHandle) :
    ViewModel() {

    var productName: String? = null
    var variantFlagArgs = 0
    var productPrice: Int = 0
    var variantArg = "RAM 16GB"
    var isFav: Boolean = false
    var checkedChipId: Int = 0
    var variant = MutableLiveData(0)
    var product: Product = Product(
        productId = "",
        brand = "",
        description = "",
        image = "",
        productName = "",
        productPrice = 0,
        productRating = 0.0,
        variantName = "",
        variantPrice = 0,
        sale = 0,
        stock = 0,
        store = "",
        totalRating = 0,
        totalReview = 0,
        totalSatisfaction = 0
    )
    var checkoutItems = mutableListOf<Product>()

    var productId = state.get<String>(PRODUCT_ID)

    // State handle
    fun saveId(id: String) {
        state[PRODUCT_ID] = id
    }

    // Api
    private var _productResponse: MutableLiveData<Resource<ProductResponse>> =
        MutableLiveData<Resource<ProductResponse>>()
    val productResponse: LiveData<Resource<ProductResponse>> = _productResponse

    private var _productResponse2: MutableStateFlow<Resource<ProductResponse>?> =
        MutableStateFlow(null)
    val productResponse2: StateFlow<Resource<ProductResponse>?> = _productResponse2.asStateFlow()

    fun productDetail() {
        viewModelScope.launch {
            _productResponse.value = Resource.Loading
            _productResponse2.value = Resource.Loading
            repository.productDetail(productId).collect { value ->
                _productResponse.value = value
                _productResponse2.value = value
            }
        }
    }

    // Database
    fun insertFav(product: Product) {
        viewModelScope.launch {
            repository.insertFav(product)
        }
    }

    fun deleteFav(id: String) {
        viewModelScope.launch {
            repository.deleteItemFav(id)
        }
    }

//    fun getIsFav(id: String) = repository.getIsFav(id).asLiveData()

    fun getIsFav(id: String) = repository.getIsFav(id).asLiveData()

    suspend fun addChart(product: Product, action: Boolean): String {
        return repository.addChart(product, action)
    }

    init {
        productDetail()
    }

    companion object {
        const val PRODUCT_ID = "productId"
    }
}
