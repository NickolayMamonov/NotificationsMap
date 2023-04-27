package com.example.notificationsmap.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.notificationsmap.data.entities.MarkerEntity

@Dao
interface MarkerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMarkerPos(marker: MarkerEntity)

    @Delete
    suspend fun deleteMarkerPos(pos: MarkerEntity)

    @Query("SELECT * FROM markers")
    suspend fun getAllMarkers(): List<MarkerEntity>

}