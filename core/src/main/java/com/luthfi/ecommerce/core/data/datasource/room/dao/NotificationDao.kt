package com.luthfi.ecommerce.core.data.datasource.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.luthfi.ecommerce.core.data.datasource.room.entity.NotificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notification")
    fun getAllNotification(): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Query("UPDATE notification SET isChecked = :newIsChecked WHERE id = :id")
    suspend fun updateIsCheckedNotification(id: Int, newIsChecked: Boolean)
}
