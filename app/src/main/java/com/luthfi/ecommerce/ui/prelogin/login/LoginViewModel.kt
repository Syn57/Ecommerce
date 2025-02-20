package com.luthfi.ecommerce.ui.prelogin.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luthfi.ecommerce.core.data.datasource.api.Resource
import com.luthfi.ecommerce.core.data.datasource.api.body.LoginBody
import com.luthfi.ecommerce.core.data.datasource.api.responses.LoginResponse
import com.luthfi.ecommerce.core.domain.repository.Repository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LoginViewModel(private val repository: Repository) : ViewModel() {

    // Preference
    fun prefGetIsOnboard(): Boolean {
        return runBlocking {
            repository.getIsOnboard().first()
        }
    }

    fun prefSetIsLogin(value: Boolean) {
        runBlocking {
            repository.setIsLogin(value)
        }
    }

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

    // Api
    private var _loginResponse: MutableLiveData<Resource<LoginResponse>> =
        MutableLiveData<Resource<LoginResponse>>()
    val loginResponse: LiveData<Resource<LoginResponse>> = _loginResponse

    fun login(body: LoginBody) {
        viewModelScope.launch {
            _loginResponse.value = Resource.Loading
            repository.login(body).collect { value ->
                _loginResponse.value = value
            }
        }
    }

    fun prefSetUsername(value: String) {
        runBlocking {
            repository.setUsername(value)
        }
    }
}
