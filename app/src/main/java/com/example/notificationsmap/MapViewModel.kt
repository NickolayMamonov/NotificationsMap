package com.example.notificationsmap

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.notificationsmap.data.database.MarkerDao
import com.example.notificationsmap.data.entities.Marker

class MapViewModel(private val markerDao: MarkerDao): ViewModel() {
    fun getMarkerPositions(): LiveData<List<Marker>>{
        return markerDao.getMarkerPositions()
    }

    suspend fun insertMarkerPos(marker: Marker){
        markerDao.insertMarkerPos(marker)
    }

    suspend fun deleteMarkerPos(marker: Marker){
        markerDao.deleteMarkerPos(marker)
    }

}