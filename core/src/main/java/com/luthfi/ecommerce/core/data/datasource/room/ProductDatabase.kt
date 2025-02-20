package com.luthfi.ecommerce.core.data.datasource.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.luthfi.ecommerce.core.data.datasource.room.dao.ChartDao
import com.luthfi.ecommerce.core.data.datasource.room.dao.FavoriteDao
import com.luthfi.ecommerce.core.data.datasource.room.dao.NotificationDao
import com.luthfi.ecommerce.core.data.datasource.room.entity.ChartEntity
import com.luthfi.ecommerce.core.data.datasource.room.entity.FavoriteEntity
import com.luthfi.ecommerce.core.data.datasource.room.entity.NotificationEntity

@Database(
    entities = [ChartEntity::class, FavoriteEntity::class, NotificationEntity::class],
    version = 2
)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun chartDao(): ChartDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun notificationDao(): NotificationDao
}
