package com.example.notificationsmap.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.notificationsmap.data.entities.Marker

@Dao
interface MarkerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMarkerPos(pos: Marker)

    @Delete
    suspend fun deleteMarkerPos(pos: Marker)

    @Query("SELECT * FROM markers")
    fun getMarkerPositions(): LiveData<List<Marker>>

}