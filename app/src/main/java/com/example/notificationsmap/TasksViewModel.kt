package com.example.notificationsmap

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.notificationsmap.data.MarkerRepo
import com.example.notificationsmap.data.database.MarkerDatabase
import com.example.notificationsmap.data.entities.MarkerEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TasksViewModel(application: Application) : AndroidViewModel(application) {
    private var repo: MarkerRepo

    init {
        val markerDao = MarkerDatabase.getDatabase(application).markerDao()
        repo = MarkerRepo(markerDao)

    }
   suspend fun getAllMarkers(): List<MarkerEntity> {
        return withContext(Dispatchers.IO) {
            repo.getAllMarkers()
        }
    }
}