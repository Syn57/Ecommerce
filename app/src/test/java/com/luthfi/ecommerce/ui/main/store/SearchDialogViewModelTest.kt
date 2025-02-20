package com.luthfi.ecommerce.ui.main.store

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.luthfi.ecommerce.core.data.datasource.api.Resource
import com.luthfi.ecommerce.core.domain.repository.Repository
import com.luthfi.ecommerce.ui.MainDispatcherRule
import com.luthfi.ecommerce.ui.main.DummyData.search
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
class SearchDialogViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val coroutineRule = InstantTaskExecutorRule()

    private lateinit var viewModel: SearchDialogViewModel
    private val repository = mock<Repository>()

    @Before
    fun setUp() {
        viewModel = SearchDialogViewModel(repository)
    }

    @Test
    fun `test search success`() = runTest {
        whenever(repository.search("")).thenReturn(
            flowOf(Resource.Success(search))
        )
        viewModel.search("")
        val actualData = viewModel.searchResponse.getOrAwaitValue()
        val expectedData = Resource.Success(search)
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test review loading`() = runTest {
        whenever(repository.search("")).thenReturn(
            flowOf(Resource.Loading)
        )
        viewModel.search("")
        val actualData = viewModel.searchResponse.getOrAwaitValue()
        val expectedData = Resource.Loading
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test review error`() = runTest {
        whenever(repository.search("")).thenReturn(
            flowOf(Resource.Error("Error"))
        )
        viewModel.search("")
        val actualData = viewModel.searchResponse.getOrAwaitValue()
        val expectedData = Resource.Error("Error")
        assertEquals(expectedData, actualData)
    }
}
