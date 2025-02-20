package com.luthfi.ecommerce.ui.main.profile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.luthfi.ecommerce.core.data.datasource.api.Resource
import com.luthfi.ecommerce.core.domain.repository.Repository
import com.luthfi.ecommerce.ui.MainDispatcherRule
import com.luthfi.ecommerce.ui.main.DummyData.profile
import com.luthfi.ecommerce.utils.getOrAwaitValue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class ProfileViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val coroutineRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ProfileViewModel
    private val repository = Mockito.mock<Repository>()

    @Before
    fun setUp() {
        viewModel = ProfileViewModel(repository)
    }

    @Test
    fun `test profile success`() = runTest {
        val requestBody = "".toRequestBody("text/plain".toMediaType())
        whenever(repository.profile(requestBody, null)).thenReturn(
            flowOf(Resource.Success(profile))
        )
        viewModel.profile(requestBody, null)
        val actualData = viewModel.profileResponse.getOrAwaitValue()
        val expectedData = Resource.Success(profile)
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test profile loading`() = runTest {
        val requestBody = "".toRequestBody("text/plain".toMediaType())
        whenever(repository.profile(requestBody, null)).thenReturn(
            flowOf(Resource.Loading)
        )
        viewModel.profile(requestBody, null)
        val actualData = viewModel.profileResponse.getOrAwaitValue()
        val expectedData = Resource.Loading
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test review error`() = runTest {
        val requestBody = "".toRequestBody("text/plain".toMediaType())
        whenever(repository.profile(requestBody, null)).thenReturn(
            flowOf(Resource.Error("Error"))
        )
        viewModel.profile(requestBody, null)
        val actualData = viewModel.profileResponse.getOrAwaitValue()
        val expectedData = Resource.Error("Error")
        assertEquals(expectedData, actualData)
    }

    @Test
    fun prefSetUsername() = runTest {
        whenever(repository.setUsername("")).thenReturn(Unit)
        val actualData = viewModel.prefSetUsername("")
        val expectedData = Unit
        assertEquals(expectedData, actualData)
    }
}
