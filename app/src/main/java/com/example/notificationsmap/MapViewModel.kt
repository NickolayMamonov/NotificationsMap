package com.example.notificationsmap

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.notificationsmap.data.TaskRepo
import com.example.notificationsmap.data.database.MarkerDatabase
import com.example.notificationsmap.data.entities.ActiveTaskEntity
import com.example.notificationsmap.data.entities.MarkerEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class MapViewModel(application: Application) : AndroidViewModel(application) {
    private var repo: TaskRepo

    init {
        val markerDao = MarkerDatabase.getDatabase(application).markerDao()
        repo = TaskRepo(markerDao)

    }
    suspend fun getAllMarkers(): List<ActiveTaskEntity> {
        return withContext(Dispatchers.IO) {
            repo.getAllTasks()
        }
    }

}