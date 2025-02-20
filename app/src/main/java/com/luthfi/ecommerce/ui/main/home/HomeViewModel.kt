package com.luthfi.ecommerce.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.luthfi.ecommerce.core.domain.repository.Repository
import kotlinx.coroutines.runBlocking

class HomeViewModel(private val repository: Repository) : ViewModel() {

    fun prefLogout() {
        runBlocking {
            repository.logout()
        }
    }

    fun prefGetLocale() = repository.getLocale().asLiveData()

    fun prefSetIsLight(value: Boolean) {
        runBlocking {
            repository.setIsLight(value)
        }
    }

    fun prefSetLocale(value: String) {
        runBlocking {
            repository.setLocale(value)
        }
    }

    fun prefGetIsLight() = repository.getIsLight().asLiveData()
}
