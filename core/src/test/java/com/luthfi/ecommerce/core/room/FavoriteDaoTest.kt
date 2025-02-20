package com.luthfi.ecommerce.core.room

// import com.luthfi.ecommerce.core.room.dummy.DummyEntities.favorite
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.luthfi.ecommerce.core.data.datasource.room.ProductDatabase
import com.luthfi.ecommerce.core.data.datasource.room.dao.FavoriteDao
import com.luthfi.ecommerce.core.room.dummy.DummyEntities.favorite
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class FavoriteDaoTest {
    private lateinit var favoriteDao: FavoriteDao
    private lateinit var db: ProductDatabase

    @Before
    fun initiation() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, ProductDatabase::class.java
        ).build()
        favoriteDao = db.favoriteDao()
    }

    @After
    fun termination() {
        db.close()
    }

    @Test
    fun `get and insert fav test`() = runTest {
        favoriteDao.insertFav(favorite)
        val actualData = favoriteDao.getAllFav().first()
        val expectedData = listOf(favorite)
        assertEquals(expectedData, actualData)
    }

    @Test
    fun `delete fav test`() = runTest {
        favoriteDao.insertFav(favorite)
        favoriteDao.deleteItemFav(favorite.productId)
        val actualData = favoriteDao.getAllFav().first()
        val expectedData = 0
        assertEquals(expectedData, actualData.size)
    }

    @Test
    fun `get isFav test`() = runTest {
        favoriteDao.insertFav(favorite)
        val actualData = favoriteDao.getIsFav(favorite.productId).first()
        val expectedData = true
        assertEquals(expectedData, actualData)
    }
}
