package com.luthfi.ecommerce.ui.main.chart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.luthfi.ecommerce.core.domain.model.Product
import com.luthfi.ecommerce.core.domain.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChartViewModel(private val repository: Repository) : ViewModel() {

    var checkoutItems = mutableListOf<Product>()

    val getAllChart = repository.getAllChart().asLiveData()

    fun deleteChart(id: String) {
        viewModelScope.launch {
            repository.deleteItemChart(id)
        }
    }

    fun allCheck(value: Boolean) {
        viewModelScope.launch {
            repository.updateCheckAllChart(value)
        }
    }

    // Button item recycler view functionality
    fun updateChart(product: Product, action: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addChart(product, action)
        }
    }

    fun updateIsChecked(id: String, value: Boolean) {
        viewModelScope.launch {
            repository.updateIsCheckedChart(id, value)
        }
    }

    fun deleteAllChecked() {
        viewModelScope.launch {
            repository.deleteCheckedChart()
        }
    }
}
