package com.luthfi.ecommerce.ui.main.store

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.luthfi.ecommerce.core.data.datasource.api.body.ProductsBody
import com.luthfi.ecommerce.core.domain.repository.Repository
import com.luthfi.ecommerce.ui.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class StoreViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val coroutineRule = InstantTaskExecutorRule()

    private lateinit var viewModel: StoreViewModel
    private val repository = mock<Repository>()

    @Before
    fun setUp() {
        viewModel = StoreViewModel((repository))
    }

//    @Test
//    fun getProducts() = runTest(UnconfinedTestDispatcher()){
//        whenever(repository.product(productsBody = null)).thenReturn(flowOf(PagingData.from(products.data.items)))
//        val actualData = viewModel.products().asSnapshot()
//        val expectedData = listOf(products.data.items)
//        assertEquals(expectedData, actualData)
//    }

    @Test
    fun `test update filter`() = runTest {
        viewModel.updateFilter(sort = "", category = "", lowest = 0, highest = 0)
        val actualData = viewModel.productsBody.value
        val expectedData = ProductsBody(
            search = null,
            brand = "",
            lowest = 0,
            highest = 0,
            sort = "",
            limit = 10,
            page = 1
        )
        assertEquals(expectedData, actualData)
    }

    @Test
    fun updateSearch() = runTest {
        viewModel.updateSearch("")
        val actualData = viewModel.productsBody.value
        val expectedData = ProductsBody(
            search = "",
            brand = null,
            lowest = null,
            highest = null,
            sort = null,
            limit = 10,
            page = 1
        )
        assertEquals(expectedData, actualData)
    }
}
