package com.luthfi.ecommerce.core.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.luthfi.ecommerce.core.data.datasource.room.ProductDatabase
import com.luthfi.ecommerce.core.data.datasource.room.dao.NotificationDao
import com.luthfi.ecommerce.core.room.dummy.DummyEntities.notification
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NotificationDaoTest {

    private lateinit var notificationDao: NotificationDao
    private lateinit var db: ProductDatabase

    @Before
    fun initiation() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, ProductDatabase::class.java
        ).build()
        notificationDao = db.notificationDao()
    }

    @After
    fun termination() {
        db.close()
    }

    @Test
    fun `get and insert notification test`() = runTest {
        notificationDao.insertNotification(notification)
        val actualData = notificationDao.getAllNotification().first()
        val expectedData = listOf(notification)
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `update is read test`() = runTest {
        notificationDao.insertNotification(notification)
        notificationDao.updateIsCheckedNotification(notification.id, true)
        val actualData = notificationDao.getAllNotification().first()
        val expectedData = listOf(notification.copy(isChecked = true))
        assertEquals(expectedData, actualData)
    }
}
