package com.luthfi.ecommerce.ui.main.status

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luthfi.ecommerce.core.data.datasource.api.Resource
import com.luthfi.ecommerce.core.data.datasource.api.body.RatingBody
import com.luthfi.ecommerce.core.data.datasource.api.responses.RatingResponse
import com.luthfi.ecommerce.core.domain.repository.Repository
import kotlinx.coroutines.launch

class StatusViewModel(private val repository: Repository) : ViewModel() {

    var ratingBody = RatingBody(
        invoiceId = null,
        rating = null,
        review = null
    )

    private var _ratingResponse = MutableLiveData<Resource<RatingResponse>>()
    val ratingResponse: LiveData<Resource<RatingResponse>> = _ratingResponse

    fun rating() {
        viewModelScope.launch {
            _ratingResponse.value = Resource.Loading
            repository.rating(ratingBody).collect { value ->
                _ratingResponse.value = value
            }
        }
    }
}
