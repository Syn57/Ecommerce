package com.luthfi.ecommerce.ui.main.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.luthfi.ecommerce.core.data.datasource.api.Resource
import com.luthfi.ecommerce.core.domain.repository.Repository
import com.luthfi.ecommerce.ui.MainDispatcherRule
import com.luthfi.ecommerce.ui.main.DummyData.product
import com.luthfi.ecommerce.ui.main.DummyData.productDetailResponse
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
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class DetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val coroutineRule = InstantTaskExecutorRule()

    private lateinit var viewModel: DetailViewModel
    private val repository = Mockito.mock<Repository>()
    private val savedStateHandle = SavedStateHandle(mapOf("productId" to product.productId))

    @Before
    fun setUp() {
        whenever(repository.productDetail(product.productId)).thenReturn(
            flowOf(
                Resource.Success(productDetailResponse)
            )
        )
        viewModel = DetailViewModel(repository, savedStateHandle)
        viewModel.productId = product.productId
    }

    @Test
    fun `test set and get productName`() {
        viewModel.productName = ""
        val actualData = viewModel.productName
        val expectedData = ""
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test set and get productPrice`() {
        viewModel.productPrice = 0
        val actualData = viewModel.productPrice
        val expectedData = 0
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test set and get isFav`() {
        viewModel.isFav = true
        val actualData = viewModel.isFav
        val expectedData = true
        assertEquals(expectedData, actualData)
    }

    @Test
    fun saveId() {
        viewModel.saveId(product.productId)
        val actualData = viewModel.productId
        val expectedData = product.productId
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test product detail`() = runTest {
        viewModel.productId = product.productId
        whenever(repository.productDetail(product.productId)).thenReturn(
            flowOf(
                Resource.Success(
                    productDetailResponse
                )
            )
        )
        viewModel.productDetail()
        val actualData = viewModel.productResponse.getOrAwaitValue()
        val expectedData = Resource.Success(productDetailResponse)
        assertEquals(expectedData, actualData)
    }

    @Test
    fun insertFav() = runTest {
        whenever(repository.insertFav(product)).thenReturn(Unit)
        val actualData = viewModel.insertFav(product)
        val expectedData = Unit
        assertEquals(expectedData, actualData)
    }

    @Test
    fun deleteFav() = runTest {
        whenever(repository.deleteItemFav(product.productId)).thenReturn(Unit)
        val actualData = viewModel.deleteFav(product.productId)
        val expectedData = Unit
        assertEquals(expectedData, actualData)
    }

    @Test
    fun getIsFav() = runTest {
        whenever(repository.getIsFav(product.productId)).thenReturn(flowOf(true))
        val actualData = viewModel.getIsFav(product.productId).getOrAwaitValue()
        val expectedData = true
        assertEquals(expectedData, actualData)
    }

    @Test
    fun addChart() = runTest {
        whenever(repository.addChart(product, true)).thenReturn("cartAdded")
        val actualData = viewModel.addChart(product, true)
        val expectedData = "cartAdded"
        assertEquals(expectedData, actualData)
    }
}
