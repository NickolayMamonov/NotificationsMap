package com.example.notificationsmap

import android.app.Application
import androidx.lifecycle.*
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.notificationsmap.data.MarkerRepo
import com.example.notificationsmap.data.database.MarkerDao
import com.example.notificationsmap.data.database.MarkerDatabase
import com.example.notificationsmap.data.entities.MarkerEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MapViewModel(application: Application): AndroidViewModel(application) {
    private var repo: MarkerRepo
    init {
        val markerDao = MarkerDatabase.getDatabase(application).markerDao()
        repo = MarkerRepo(markerDao)

    }

    fun insertMarkerPos(marker: MarkerEntity){
        viewModelScope.launch {
           withContext(Dispatchers.IO){
               repo.insertMarkerPos(marker)
           }
        }
    }

    suspend fun getAllMarkers(): List<MarkerEntity>{
        return withContext(Dispatchers.IO){
            repo.getAllMarkers()
        }
    }

}