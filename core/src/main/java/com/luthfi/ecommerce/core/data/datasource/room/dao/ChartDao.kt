package com.luthfi.ecommerce.core.data.datasource.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.luthfi.ecommerce.core.data.datasource.room.entity.ChartEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChartDao {

    @Query("SELECT * FROM chart")
    fun getAllChart(): Flow<List<ChartEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChart(chart: ChartEntity)

    @Query("UPDATE chart SET count = :newCount WHERE productId = :id")
    fun updateCountChart(id: String, newCount: Int)

    @Query("UPDATE chart SET isChecked = :newIsChecked WHERE productId = :id")
    suspend fun updateIsCheckedChart(id: String, newIsChecked: Boolean)

    @Query("UPDATE chart SET isChecked = :value")
    suspend fun updateCheckAllChart(value: Boolean)

    @Query("DELETE FROM chart WHERE productId = :id")
    suspend fun deleteItemChart(id: String)

    @Query("SELECT * FROM chart WHERE productId = :id")
    fun getStockChart(id: String): ChartEntity?

    @Query("DELETE FROM chart WHERE isChecked = 1")
    suspend fun deleteCheckedChart()
}
