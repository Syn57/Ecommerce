package com.luthfi.ecommerce.ui.main.checkout

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.luthfi.ecommerce.core.data.datasource.api.Resource
import com.luthfi.ecommerce.core.data.datasource.api.body.FulfillmentBody
import com.luthfi.ecommerce.core.domain.repository.Repository
import com.luthfi.ecommerce.ui.MainDispatcherRule
import com.luthfi.ecommerce.ui.main.DummyData.fulfillment
import com.luthfi.ecommerce.ui.main.DummyData.product
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
class CheckoutViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val coroutineRule = InstantTaskExecutorRule()

    private lateinit var viewModel: CheckoutViewModel
    private val repository = Mockito.mock<Repository>()

    @Before
    fun setUp() {
        viewModel = CheckoutViewModel(repository)
    }

    @Test
    fun `test increase cart success`() = runTest {
        viewModel.checkoutProduct.value = listOf(product)
        viewModel.increaseChart(0)
        val expectedData = listOf(product.copy(count = 2))
        val actualData = viewModel.checkoutProduct.value
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test increase cart maximum`() = runTest {
        viewModel.checkoutProduct.value = listOf(product.copy(count = 2))
        viewModel.increaseChart(0)
        val expectedData = listOf(product.copy(count = 2))
        val actualData = viewModel.checkoutProduct.value
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test decrease cart success`() {
        viewModel.checkoutProduct.value = listOf(product.copy(count = 2))
        viewModel.decreaseChart(0)
        val expectedData = listOf(product)
        val actualData = viewModel.checkoutProduct.value
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test decrease cart minimum`() {
        viewModel.checkoutProduct.value = listOf(product)
        viewModel.decreaseChart(0)
        val expectedData = listOf(product)
        val actualData = viewModel.checkoutProduct.value
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test fulfillment success`() = runTest {
        viewModel.fBody.value = FulfillmentBody(payment = null, items = listOf())
        whenever(
            repository.fulfillment(
                FulfillmentBody(
                    payment = null,
                    items = listOf()
                )
            )
        ).thenReturn(flowOf(Resource.Success(fulfillment)))
        viewModel.fulfillment()
        val actualData = viewModel.fulfillmentResponse.getOrAwaitValue()
        val expectedData = Resource.Success(fulfillment)
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test fulfillment loading`() = runTest {
        viewModel.fBody.value = FulfillmentBody(payment = null, items = listOf())
        whenever(
            repository.fulfillment(
                FulfillmentBody(
                    payment = null,
                    items = listOf()
                )
            )
        ).thenReturn(flowOf(Resource.Loading))
        viewModel.fulfillment()
        val actualData = viewModel.fulfillmentResponse.getOrAwaitValue()
        val expectedData = Resource.Loading
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test fulfillment error`() = runTest {
        viewModel.fBody.value = FulfillmentBody(payment = null, items = listOf())
        whenever(
            repository.fulfillment(
                FulfillmentBody(
                    payment = null,
                    items = listOf()
                )
            )
        ).thenReturn(flowOf(Resource.Error("Error")))
        viewModel.fulfillment()
        val actualData = viewModel.fulfillmentResponse.getOrAwaitValue()
        val expectedData = Resource.Error("Error")
        assertEquals(expectedData, actualData)
    }

    @Test
    fun addItemToBuy() {
        viewModel.addItemToBuy(
            listOf(
                FulfillmentBody.Item(
                    productId = null,
                    variantName = null,
                    quantity = null
                )
            )
        )
        val actualData = viewModel.fBody.value
        val expectedData = FulfillmentBody(
            payment = null,
            items = listOf(
                FulfillmentBody.Item(
                    productId = null,
                    variantName = null,
                    quantity = null
                )
            )
        )
        assertEquals(expectedData, actualData)
    }

    @Test
    fun addPaymentMethod() {
        viewModel.addPaymentMethod("")
        val actualData = viewModel.fBody.value?.payment
        val expectedData = ""
        assertEquals(expectedData, actualData)
    }

    @Test
    fun deleteAllChecked() = runTest {
        whenever(repository.deleteCheckedChart()).thenReturn(Unit)
        val actualData = viewModel.deleteAllChecked()
        val expectedData = Unit
        assertEquals(expectedData, actualData)
    }
}
