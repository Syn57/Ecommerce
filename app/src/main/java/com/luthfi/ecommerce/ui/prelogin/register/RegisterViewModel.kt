package com.luthfi.ecommerce.ui.prelogin.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luthfi.ecommerce.core.data.datasource.api.Resource
import com.luthfi.ecommerce.core.data.datasource.api.body.LoginBody
import com.luthfi.ecommerce.core.data.datasource.api.responses.RegisterResponse
import com.luthfi.ecommerce.core.domain.repository.Repository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class RegisterViewModel(private val repository: Repository) : ViewModel() {

    private var _registerResponse: MutableLiveData<Resource<RegisterResponse>> =
        MutableLiveData<Resource<RegisterResponse>>()
    val registerResponse: LiveData<Resource<RegisterResponse>> = _registerResponse

    // Preference
    fun prefSetAccessToken(value: String) {
        runBlocking {
            repository.setAccessToken(value)
        }
    }

    fun prefSetRefreshToken(value: String) {
        runBlocking {
            repository.setRefreshToken(value)
        }
    }

    fun prefSetIsLogin(value: Boolean) {
        runBlocking {
            repository.setIsLogin(value)
        }
    }

    // Api
    fun register(body: LoginBody) {
        viewModelScope.launch {
            _registerResponse.value = Resource.Loading
            repository.register(body).collect { value ->
                _registerResponse.value = value
            }
        }
    }
}
