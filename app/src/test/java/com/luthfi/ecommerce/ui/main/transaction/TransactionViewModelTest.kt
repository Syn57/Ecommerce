package com.luthfi.ecommerce.ui.main.transaction

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.luthfi.ecommerce.core.data.datasource.api.Resource
import com.luthfi.ecommerce.core.domain.repository.Repository
import com.luthfi.ecommerce.ui.MainDispatcherRule
import com.luthfi.ecommerce.ui.main.DummyData.transaction
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
class TransactionViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val coroutineRule = InstantTaskExecutorRule()

    private lateinit var viewModel: TransactionViewModel
    private val repository = Mockito.mock<Repository>()

    @Before
    fun setUp() {
        runTest {
            whenever(repository.transaction()).thenReturn(
                flowOf(Resource.Success(transaction))
            )
        }
        viewModel = TransactionViewModel((repository))
    }

    @Test
    fun `test transaction success`() = runTest {
        whenever(repository.transaction()).thenReturn(
            flowOf(Resource.Success(transaction))
        )
        viewModel.transaction()
        val actualData = viewModel.transactionResponse.getOrAwaitValue()
        val expectedData = Resource.Success(transaction)
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test transaction loading`() = runTest {
        whenever(repository.transaction()).thenReturn(
            flowOf(Resource.Loading)
        )
        viewModel.transaction()
        val actualData = viewModel.transactionResponse.getOrAwaitValue()
        val expectedData = Resource.Loading
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test transaction error`() = runTest {
        whenever(repository.transaction()).thenReturn(
            flowOf(Resource.Error("Error"))
        )
        viewModel.transaction()
        val actualData = viewModel.transactionResponse.getOrAwaitValue()
        val expectedData = Resource.Error("Error")
        assertEquals(expectedData, actualData)
    }
}
