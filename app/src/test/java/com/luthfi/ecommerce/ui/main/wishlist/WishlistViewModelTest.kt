package com.luthfi.ecommerce.ui.main.wishlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.luthfi.ecommerce.core.domain.repository.Repository
import com.luthfi.ecommerce.ui.MainDispatcherRule
import com.luthfi.ecommerce.ui.main.DummyData.product
import com.luthfi.ecommerce.utils.getOrAwaitValue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class WishlistViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val coroutineRule = InstantTaskExecutorRule()

    private lateinit var viewModel: WishlistViewModel
    private val repository = mock<Repository>()

    @Before
    fun setUp() {
        viewModel = WishlistViewModel((repository))
    }

    @Test
    fun `test delete fav`() = runTest {
        whenever(repository.deleteItemFav("")).thenReturn(Unit)
        val actualData = viewModel.deleteFav("")
        val expectedData = Unit
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test add cart`() = runTest {
        whenever(repository.addChart(product, true)).thenReturn("cartAdded")
        val actualData = viewModel.addChart(product, true)
        val expectedData = "cartAdded"
        assertEquals(expectedData, actualData)
    }

    @Test
    fun getAllFav() {
        whenever(repository.getAllFav()).thenReturn(flowOf(listOf(product)))
        val actualData = viewModel.getAllFav().getOrAwaitValue()
        val expectedData = listOf(product)
        assertEquals(expectedData, actualData)
    }
}
