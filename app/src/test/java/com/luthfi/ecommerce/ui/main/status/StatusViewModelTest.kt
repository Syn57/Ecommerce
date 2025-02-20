package com.luthfi.ecommerce.ui.main.status

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.luthfi.ecommerce.core.data.datasource.api.Resource
import com.luthfi.ecommerce.core.data.datasource.api.body.RatingBody
import com.luthfi.ecommerce.core.domain.repository.Repository
import com.luthfi.ecommerce.ui.MainDispatcherRule
import com.luthfi.ecommerce.ui.main.DummyData.rating
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
class StatusViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val coroutineRule = InstantTaskExecutorRule()

    private lateinit var viewModel: StatusViewModel
    private val repository = Mockito.mock<Repository>()

    @Before
    fun setUp() {
        viewModel = StatusViewModel(repository)
    }

    @Test
    fun `test status success`() = runTest {
        whenever(
            repository.rating(
                RatingBody(
                    invoiceId = null,
                    rating = null,
                    review = null
                )
            )
        ).thenReturn(
            flowOf(Resource.Success(rating))
        )
        viewModel.rating()
        val actualData = viewModel.ratingResponse.getOrAwaitValue()
        val expectedData = Resource.Success(rating)
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test status loading`() = runTest {
        whenever(
            repository.rating(
                RatingBody(
                    invoiceId = null,
                    rating = null,
                    review = null
                )
            )
        ).thenReturn(
            flowOf(Resource.Loading)
        )
        viewModel.rating()
        val actualData = viewModel.ratingResponse.getOrAwaitValue()
        val expectedData = Resource.Loading
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test status error`() = runTest {
        whenever(
            repository.rating(
                RatingBody(
                    invoiceId = null,
                    rating = null,
                    review = null
                )
            )
        ).thenReturn(
            flowOf(Resource.Error("Error"))
        )
        viewModel.rating()
        val actualData = viewModel.ratingResponse.getOrAwaitValue()
        val expectedData = Resource.Error("Error")
        assertEquals(expectedData, actualData)
    }
}
