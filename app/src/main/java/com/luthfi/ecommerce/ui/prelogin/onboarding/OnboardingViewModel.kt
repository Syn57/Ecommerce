package com.luthfi.ecommerce.ui.prelogin.onboarding

import androidx.lifecycle.ViewModel
import com.luthfi.ecommerce.core.domain.repository.Repository
import kotlinx.coroutines.runBlocking

class OnboardingViewModel(private val repository: Repository) : ViewModel() {
    fun prefSetIsOnboard(value: Boolean) {
        runBlocking {
            repository.setIsOnBoard(value)
        }
    }
}
