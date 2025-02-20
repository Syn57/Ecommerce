package com.luthfi.ecommerce.ui.main.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.luthfi.ecommerce.core.domain.repository.Repository
import com.luthfi.ecommerce.ui.MainDispatcherRule
import com.luthfi.ecommerce.utils.getOrAwaitValue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val coroutineRule = InstantTaskExecutorRule()

    private lateinit var viewModel: HomeViewModel
    private val repository = Mockito.mock<Repository>()

    @Before
    fun setUp() {
        viewModel = HomeViewModel(repository)
    }

    @Test
    fun prefLogout() = runTest {
        val actualData = viewModel.prefLogout()
        val expectedData = Unit
        assertEquals(expectedData, actualData)
        verify(repository).logout()
    }

    @Test
    fun prefGetLocale() {
        whenever(repository.getLocale()).thenReturn(flowOf("id"))
        val actualData = viewModel.prefGetLocale().getOrAwaitValue()
        val expectedData = "id"
        assertEquals(expectedData, actualData)
    }

    @Test
    fun prefSetIsLight() = runTest {
        val actualData = viewModel.prefSetIsLight(true)
        val expectedData = Unit
        assertEquals(expectedData, actualData)
        verify(repository).setIsLight(true)
    }

    @Test
    fun prefSetLocale() = runTest {
        val actualData = viewModel.prefSetLocale("id")
        val expectedData = Unit
        assertEquals(expectedData, actualData)
        verify(repository).setLocale("id")
    }

    @Test
    fun prefGetIsLight() {
        whenever(repository.getIsLight()).thenReturn(flowOf(true))
        val actualData = viewModel.prefGetIsLight().getOrAwaitValue()
        val expectedData = true
        assertEquals(expectedData, actualData)
    }
}
