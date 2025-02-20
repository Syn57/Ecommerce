package com.luthfi.ecommerce.ui.main.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.luthfi.ecommerce.core.domain.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationViewModel(private val repository: Repository) : ViewModel() {

    fun getPromoNotification() = repository.getAllNotification().asLiveData()

    fun updateIsCheck(id: Int, value: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateCheckNotification(id, value)
        }
    }
}
