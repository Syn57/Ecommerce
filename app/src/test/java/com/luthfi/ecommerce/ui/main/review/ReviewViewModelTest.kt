package com.luthfi.ecommerce.ui.main.review

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.luthfi.ecommerce.core.data.datasource.api.Resource
import com.luthfi.ecommerce.core.domain.repository.Repository
import com.luthfi.ecommerce.ui.MainDispatcherRule
import com.luthfi.ecommerce.ui.main.DummyData.review
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
class ReviewViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val coroutineRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ReviewViewModel
    private val repository = Mockito.mock<Repository>()

    @Before
    fun setUp() {
        viewModel = ReviewViewModel(repository)
    }

    @Test
    fun `test review success`() = runTest {
        whenever(repository.productReview("")).thenReturn(
            flowOf(Resource.Success(review))
        )
        viewModel.review("")
        val actualData = viewModel.reviewResponse2.getOrAwaitValue()
        val expectedData = Resource.Success(review)
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test review loading`() = runTest {
        whenever(repository.productReview("")).thenReturn(
            flowOf(Resource.Loading)
        )
        viewModel.review("")
        val actualData = viewModel.reviewResponse2.getOrAwaitValue()
        val expectedData = Resource.Loading
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test review error`() = runTest {
        whenever(repository.productReview("")).thenReturn(
            flowOf(Resource.Error("Error"))
        )
        viewModel.review("")
        val actualData = viewModel.reviewResponse2.getOrAwaitValue()
        val expectedData = Resource.Error("Error")
        assertEquals(expectedData, actualData)
    }
}
