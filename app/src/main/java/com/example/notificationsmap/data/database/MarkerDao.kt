package com.example.notificationsmap.data.database


import androidx.room.*
import com.example.notificationsmap.data.entities.ActiveTaskEntity

@Dao
interface MarkerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMarkerPos(task: ActiveTaskEntity)

    @Update
    suspend fun updateTask(task:ActiveTaskEntity)

    @Delete
    suspend fun deleteTask(task: ActiveTaskEntity)

    @Query("SELECT * FROM Taskstest")
    suspend fun getAllTasks(): List<ActiveTaskEntity>

    @Query("SELECT * FROM Taskstest WHERE id = :id")
    suspend fun getTaskById(id: Long): ActiveTaskEntity

}