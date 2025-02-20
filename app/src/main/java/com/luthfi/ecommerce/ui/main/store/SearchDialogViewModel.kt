package com.luthfi.ecommerce.ui.main.store

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luthfi.ecommerce.core.data.datasource.api.Resource
import com.luthfi.ecommerce.core.data.datasource.api.responses.SearchResponse
import com.luthfi.ecommerce.core.domain.repository.Repository
import kotlinx.coroutines.launch

class SearchDialogViewModel(private val repository: Repository) : ViewModel() {

    var search: String? = null

    private var _searchResponse: MutableLiveData<Resource<SearchResponse>> =
        MutableLiveData<Resource<SearchResponse>>()
    val searchResponse: LiveData<Resource<SearchResponse>> = _searchResponse

    fun search(query: String?) {
        viewModelScope.launch {
            _searchResponse.value = Resource.Loading
            repository.search(query).collect { value ->
                _searchResponse.value = value
            }
        }
    }
}
