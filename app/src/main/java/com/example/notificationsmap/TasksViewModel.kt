package com.example.notificationsmap

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.notificationsmap.data.TaskRepo
import com.example.notificationsmap.data.database.MarkerDatabase
import com.example.notificationsmap.data.entities.ActiveTaskEntity
import com.example.notificationsmap.data.entities.MarkerEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TasksViewModel(application: Application) : AndroidViewModel(application) {
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
    fun updateMarker(task: ActiveTaskEntity){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                repo.updateMarker(task)
            }
        }
    }
    fun deleteMarker(task: ActiveTaskEntity){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                repo.deleteMarker(task)
            }
        }
    }

    fun getTaskById(id: Long){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                repo.getTaskById(id)
            }
        }
    }
}