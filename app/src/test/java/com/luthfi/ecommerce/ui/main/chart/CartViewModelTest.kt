package com.luthfi.ecommerce.ui.main.chart

import com.luthfi.ecommerce.core.domain.repository.Repository
import com.luthfi.ecommerce.ui.MainDispatcherRule
import com.luthfi.ecommerce.ui.main.DummyData.checkoutItems
import com.luthfi.ecommerce.ui.main.DummyData.product
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class CartViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: ChartViewModel
    private val repository = mock<Repository>()

    @Before
    fun setUp() {
        whenever(repository.getAllChart()).thenReturn(flowOf(listOf(product)))
        viewModel = ChartViewModel(repository)
    }

    @Test
    fun `test get and set checkout item`() {
        viewModel.checkoutItems = checkoutItems.toMutableList()
        val actualData = viewModel.checkoutItems
        val expectedData = checkoutItems.toMutableList()
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test delete all cart`() = runTest {
        whenever(repository.deleteItemChart("")).thenReturn(Unit)
        val actualData = viewModel.deleteChart("")
        val expectedData = Unit
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test check all cart`() = runTest {
        whenever(repository.updateCheckAllChart(true)).thenReturn(Unit)
        val actualData = viewModel.allCheck(true)
        val expectedData = Unit
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test update cart`() = runTest {
//        whenever(repository.addChart(product, true)).thenReturn("cartAdded")
        val actualData = viewModel.updateChart(product, true)
        val expectedData = Unit
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test update is checked`() = runTest {
        whenever(repository.updateIsCheckedChart("", true)).thenReturn(Unit)
        val actualData = viewModel.updateIsChecked("", true)
        val expectedData = Unit
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test delete all checked`() = runTest {
        whenever(repository.deleteCheckedChart()).thenReturn(Unit)
        val actualData = viewModel.deleteAllChecked()
        val expectedData = Unit
        assertEquals(expectedData, actualData)
    }
}
