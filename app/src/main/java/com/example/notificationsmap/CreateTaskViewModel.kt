package com.example.notificationsmap

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.notificationsmap.data.TaskRepo
import com.example.notificationsmap.data.database.MarkerDatabase
import com.example.notificationsmap.data.entities.ActiveTaskEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateTaskViewModel(application: Application): AndroidViewModel(application) {
    private var repo: TaskRepo

    init {
        val markerDao = MarkerDatabase.getDatabase(application).markerDao()
        repo = TaskRepo(markerDao)

    }

    fun insertMarkerPos(task: ActiveTaskEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repo.insertMarkerPos(task)
            }
        }
    }
}