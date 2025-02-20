package com.luthfi.ecommerce.ui.prelogin.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.luthfi.ecommerce.core.data.datasource.api.Resource
import com.luthfi.ecommerce.core.data.datasource.api.body.LoginBody
import com.luthfi.ecommerce.core.domain.repository.Repository
import com.luthfi.ecommerce.ui.MainDispatcherRule
import com.luthfi.ecommerce.ui.main.DummyData.register
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
class RegisterViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val coroutineRule = InstantTaskExecutorRule()

    private lateinit var viewModel: RegisterViewModel
    private val repository = Mockito.mock<Repository>()

    @Before
    fun setUp() {
        viewModel = RegisterViewModel(repository)
    }

    @Test
    fun `test set is login`() = runTest {
        whenever(repository.setIsLogin(true)).thenReturn(Unit)
        val actualData = viewModel.prefSetIsLogin(true)
        val expectedData = Unit
        assertEquals(expectedData, actualData)
    }

    @Test
    fun prefSetAccessToken() = runTest {
        whenever(repository.setAccessToken("")).thenReturn(Unit)
        val actualData = viewModel.prefSetAccessToken("")
        val expectedData = Unit
        assertEquals(expectedData, actualData)
    }

    @Test
    fun prefSetRefreshToken() = runTest {
        whenever(repository.setRefreshToken("")).thenReturn(Unit)
        val actualData = viewModel.prefSetRefreshToken("")
        val expectedData = Unit
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test login success`() = runTest {
        whenever(repository.register(LoginBody())).thenReturn(
            flowOf(Resource.Success(register))
        )
        viewModel.register(LoginBody())
        val actualData = viewModel.registerResponse.getOrAwaitValue()
        val expectedData = Resource.Success(register)
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test login loading`() = runTest {
        whenever(repository.register(LoginBody())).thenReturn(
            flowOf(Resource.Loading)
        )
        viewModel.register(LoginBody())
        val actualData = viewModel.registerResponse.getOrAwaitValue()
        val expectedData = Resource.Loading
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test login error`() = runTest {
        whenever(repository.register(LoginBody())).thenReturn(
            flowOf(Resource.Error("Error"))
        )
        viewModel.register(LoginBody())
        val actualData = viewModel.registerResponse.getOrAwaitValue()
        val expectedData = Resource.Error("Error")
        assertEquals(expectedData, actualData)
    }
}
