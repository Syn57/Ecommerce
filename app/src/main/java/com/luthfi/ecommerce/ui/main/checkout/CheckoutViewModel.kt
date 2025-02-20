package com.luthfi.ecommerce.ui.main.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luthfi.ecommerce.core.data.datasource.api.Resource
import com.luthfi.ecommerce.core.data.datasource.api.body.FulfillmentBody
import com.luthfi.ecommerce.core.data.datasource.api.responses.FulfillmentResponse
import com.luthfi.ecommerce.core.domain.model.Product
import com.luthfi.ecommerce.core.domain.repository.Repository
import kotlinx.coroutines.launch

class CheckoutViewModel(private val repository: Repository) : ViewModel() {

    var checkoutProduct = MutableLiveData<List<Product>>()

    var fBody = MutableLiveData(
        FulfillmentBody(
            payment = null,
            items = listOf(null, null, null)
        )
    )

    val paymentMethod = MutableLiveData<HashMap<String, String?>>()

    private var _fulfillmentResponse = MutableLiveData<Resource<FulfillmentResponse>>()
    val fulfillmentResponse: LiveData<Resource<FulfillmentResponse>> = _fulfillmentResponse

    fun increaseChart(position: Int) {
        val new = checkoutProduct.value!!
        new[position].count =
            if (new[position].count == new[position].stock) new[position].stock else new[position].count + 1
        checkoutProduct.postValue(new)
    }

    fun decreaseChart(position: Int) {
        val new = checkoutProduct.value!!
        new[position].count = if (new[position].count > 1) new[position].count - 1 else 1
        checkoutProduct.postValue(new)
    }

    fun fulfillment() {
        viewModelScope.launch {
            _fulfillmentResponse.value = Resource.Loading
            repository.fulfillment(fBody.value!!).collect { value ->
                _fulfillmentResponse.value = value
            }
        }
    }

    fun addItemToBuy(items: List<FulfillmentBody.Item?>?) {
        fBody.postValue(
            fBody.value?.copy(
                items = items
            )
        )
    }

    fun addPaymentMethod(payment: String?) {
        fBody.value?.payment = payment
    }

    fun deleteAllChecked() {
        viewModelScope.launch {
            repository.deleteCheckedChart()
        }
    }
}
