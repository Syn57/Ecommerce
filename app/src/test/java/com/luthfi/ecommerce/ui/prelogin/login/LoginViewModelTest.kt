package com.luthfi.ecommerce.ui.prelogin.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.luthfi.ecommerce.core.data.datasource.api.Resource
import com.luthfi.ecommerce.core.data.datasource.api.body.LoginBody
import com.luthfi.ecommerce.core.domain.repository.Repository
import com.luthfi.ecommerce.ui.MainDispatcherRule
import com.luthfi.ecommerce.ui.main.DummyData.login
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
class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val coroutineRule = InstantTaskExecutorRule()

    private lateinit var viewModel: LoginViewModel
    private val repository = Mockito.mock<Repository>()

    @Before
    fun setUp() {
        viewModel = LoginViewModel(repository)
    }

    @Test
    fun `test get is onboard`() {
        whenever(repository.getIsOnboard()).thenReturn(flowOf(true))
        val actualData = viewModel.prefGetIsOnboard()
        val expectedData = true
        assertEquals(expectedData, actualData)
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
    fun prefSetUsername() = runTest {
        whenever(repository.setUsername("")).thenReturn(Unit)
        val actualData = viewModel.prefSetUsername("")
        val expectedData = Unit
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test login success`() = runTest {
        whenever(repository.login(LoginBody())).thenReturn(
            flowOf(Resource.Success(login))
        )
        viewModel.login(LoginBody())
        val actualData = viewModel.loginResponse.getOrAwaitValue()
        val expectedData = Resource.Success(login)
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test login loading`() = runTest {
        whenever(repository.login(LoginBody())).thenReturn(
            flowOf(Resource.Loading)
        )
        viewModel.login(LoginBody())
        val actualData = viewModel.loginResponse.getOrAwaitValue()
        val expectedData = Resource.Loading
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `test login error`() = runTest {
        whenever(repository.login(LoginBody())).thenReturn(
            flowOf(Resource.Error("Error"))
        )
        viewModel.login(LoginBody())
        val actualData = viewModel.loginResponse.getOrAwaitValue()
        val expectedData = Resource.Error("Error")
        assertEquals(expectedData, actualData)
    }
}
