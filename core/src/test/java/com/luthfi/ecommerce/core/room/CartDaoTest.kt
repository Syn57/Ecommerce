package com.luthfi.ecommerce.core.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.luthfi.ecommerce.core.data.datasource.room.ProductDatabase
import com.luthfi.ecommerce.core.data.datasource.room.dao.ChartDao
import com.luthfi.ecommerce.core.room.dummy.DummyEntities.cart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CartDaoTest {

    private lateinit var cartDao: ChartDao
    private lateinit var db: ProductDatabase

    @Before
    fun initiation() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, ProductDatabase::class.java
        ).build()
        cartDao = db.chartDao()
    }

    @After
    fun termination() {
        db.close()
    }

    @Test
    fun `get and insert cart test`() = runTest {
        cartDao.insertChart(cart)
        val actualData = cartDao.getAllChart().first()
        val expectedData = listOf(cart)
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `update count cart test`() = runTest {
        withContext(Dispatchers.IO) {
            cartDao.insertChart(cart)
            cartDao.updateCountChart(cart.productId, 2)
            val actualData = cartDao.getAllChart().first()
            val expectedData = listOf(cart.copy(count = 2))
            assertEquals(expectedData, actualData)
        }
    }

    @Test
    fun `update is checked cart test`() = runTest {
        withContext(Dispatchers.IO) {
            cartDao.insertChart(cart)
            cartDao.updateIsCheckedChart(cart.productId, true)
            val actualData = cartDao.getAllChart().first()
            val expectedData = listOf(cart.copy(isChecked = true))
            assertEquals(expectedData, actualData)
        }
    }

    @Test
    fun `update check all cart test`() = runTest {
        withContext(Dispatchers.IO) {
            cartDao.insertChart(cart)
            cartDao.updateCheckAllChart(true)
            val actualData = cartDao.getAllChart().first()
            val expectedData = listOf(cart.copy(isChecked = true))
            assertEquals(expectedData, actualData)
        }
    }

    @Test
    fun `delete item cart test`() = runTest {
        withContext(Dispatchers.IO) {
            cartDao.insertChart(cart)
            cartDao.deleteItemChart(cart.productId)
            val actualData = cartDao.getAllChart().first()
            assertEquals(0, actualData.size)
        }
    }

    @Test
    fun `get stock cart test`() = runTest {
        withContext(Dispatchers.IO) {
            cartDao.insertChart(cart)
            val actualData = cartDao.getStockChart(cart.productId)
            val expectedData = cart
            assertEquals(expectedData, actualData)
        }
    }

    @Test
    fun `delete all checked cart test`() = runTest {
        withContext(Dispatchers.IO) {
            cartDao.insertChart(cart)
            cartDao.updateCheckAllChart(true)
            cartDao.deleteCheckedChart()
            val actualData = cartDao.getAllChart().first()
            assertEquals(0, actualData.size)
        }
    }
}
