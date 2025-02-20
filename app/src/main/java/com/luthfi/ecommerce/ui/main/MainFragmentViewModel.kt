package com.luthfi.ecommerce.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.luthfi.ecommerce.core.domain.repository.Repository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MainFragmentViewModel(private val repository: Repository) : ViewModel() {

    // Preference
    fun prefGetAccessToken(): String {
        return runBlocking {
            repository.getAccessToken().first()
        }
    }

    fun prefGetUsername(): String {
        return runBlocking {
            repository.getUsername().first()
        }
    }

    fun getAllChart() = repository.getAllChart().asLiveData()

    fun getAllFav() = repository.getAllFav().asLiveData()

    fun getPromoNotification() = repository.getAllNotification().asLiveData()
}
