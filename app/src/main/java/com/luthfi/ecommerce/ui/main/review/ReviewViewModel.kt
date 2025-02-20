package com.luthfi.ecommerce.ui.main.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luthfi.ecommerce.core.data.datasource.api.Resource
import com.luthfi.ecommerce.core.data.datasource.api.responses.ReviewResponse
import com.luthfi.ecommerce.core.domain.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReviewViewModel(private val repository: Repository) : ViewModel() {

    // Api
    private var _reviewResponse2: MutableLiveData<Resource<ReviewResponse>> =
        MutableLiveData<Resource<ReviewResponse>>()
    val reviewResponse2: LiveData<Resource<ReviewResponse>> = _reviewResponse2

    private var _reviewResponse: MutableStateFlow<Resource<ReviewResponse>?> =
        MutableStateFlow(null)
    val reviewResponse: StateFlow<Resource<ReviewResponse>?> = _reviewResponse.asStateFlow()

    fun review(id: String?) {
        viewModelScope.launch {
            _reviewResponse.value = Resource.Loading
            _reviewResponse2.value = Resource.Loading
            repository.productReview(id).collect { value ->
                _reviewResponse.value = value
                _reviewResponse2.value = value
            }
        }
    }
}
