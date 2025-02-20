package com.luthfi.ecommerce.ui.prelogin.onboarding

import com.luthfi.ecommerce.core.data.RepositoryImpl
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class OnboardingViewModelTest {

    private lateinit var viewModel: OnboardingViewModel
    private val repository: RepositoryImpl = mock()

    @Before
    fun setUp() {
        viewModel = OnboardingViewModel(repository)
    }

    @Test
    fun prefSetIsOnboard() = runTest {
        whenever(repository.setIsOnBoard(true)).thenReturn(Unit)
        val actualData = viewModel.prefSetIsOnboard(true)
        val expectedData = Unit
        assertEquals(expectedData, actualData)
    }
}
