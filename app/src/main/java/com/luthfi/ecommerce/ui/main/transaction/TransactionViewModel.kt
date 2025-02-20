package com.luthfi.ecommerce.ui.main.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luthfi.ecommerce.core.data.datasource.api.Resource
import com.luthfi.ecommerce.core.data.datasource.api.responses.TransactionResponse
import com.luthfi.ecommerce.core.domain.repository.Repository
import kotlinx.coroutines.launch

class TransactionViewModel(private val repository: Repository) : ViewModel() {

    private var _transactionResponse = MutableLiveData<Resource<TransactionResponse>>()
    val transactionResponse: LiveData<Resource<TransactionResponse>> = _transactionResponse

    fun transaction() {
        viewModelScope.launch {
            _transactionResponse.value = Resource.Loading
            repository.transaction().collect { value ->
                _transactionResponse.value = value
            }
        }
    }

    init {
        transaction()
    }
}
