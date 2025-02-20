package com.luthfi.ecommerce.core.data.datasource.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.luthfi.ecommerce.core.data.datasource.room.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite")
    fun getAllFav(): Flow<List<FavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFav(fav: FavoriteEntity)

    @Query("DELETE FROM favorite WHERE productId = :id")
    suspend fun deleteItemFav(id: String)

    @Query("SELECT EXISTS (SELECT 1 FROM favorite WHERE productId = :id)")
    fun getIsFav(id: String): Flow<Boolean>
}
