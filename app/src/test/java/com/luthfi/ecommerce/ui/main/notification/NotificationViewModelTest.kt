package com.luthfi.ecommerce.ui.main.notification

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.luthfi.ecommerce.core.data.RepositoryImpl
import com.luthfi.ecommerce.ui.MainDispatcherRule
import com.luthfi.ecommerce.ui.main.DummyData.notification
import com.luthfi.ecommerce.utils.getOrAwaitValue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class NotificationViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val coroutineRule = InstantTaskExecutorRule()

    private lateinit var viewModel: NotificationViewModel
    private val repository: RepositoryImpl = mock()

    @Before
    fun setUp() {
        runBlocking { whenever(repository.updateCheckNotification(1, true)).thenReturn(Unit) }
        viewModel = NotificationViewModel(repository)
    }

    @Test
    fun getPromoNotification() {
        whenever(repository.getAllNotification()).thenReturn(flowOf(listOf(notification)))
        val actualData = viewModel.getPromoNotification().getOrAwaitValue()
        val expectedData = listOf(notification)
        assertEquals(expectedData, actualData)
    }

    @Test
    fun updateIsCheck() = runTest {
//        whenever(repository.updateCheckNotification(1, true)).thenReturn(Unit)
        val actualData = viewModel.updateIsCheck(1, true)
        val expectedData = Unit
        assertEquals(expectedData, actualData)
        verify(repository).updateCheckNotification(1, true)
    }
}
