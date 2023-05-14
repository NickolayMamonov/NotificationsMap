package com.example.notificationsmap.data.database


import androidx.room.*
import com.example.notificationsmap.data.entities.ActiveTaskEntity

@Dao
interface MarkerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMarkerPos(task: ActiveTaskEntity)

    @Delete
    suspend fun deleteMarkerPos(task: ActiveTaskEntity)

    @Query("SELECT * FROM Tasks")
    suspend fun getAllTasks(): List<ActiveTaskEntity>

}