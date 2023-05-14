package com.example.notificationsmap.data

import com.example.notificationsmap.data.database.MarkerDao
import com.example.notificationsmap.data.entities.ActiveTaskEntity

class TaskRepo(private val markerDao:MarkerDao){
    suspend fun insertMarkerPos(task: ActiveTaskEntity){
        markerDao.insertMarkerPos(task)
    }

    suspend fun getAllTasks(): List<ActiveTaskEntity>{
        return markerDao.getAllTasks()
    }

}