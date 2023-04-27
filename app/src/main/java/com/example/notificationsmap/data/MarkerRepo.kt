package com.example.notificationsmap.data

import com.example.notificationsmap.data.database.MarkerDao
import com.example.notificationsmap.data.entities.MarkerEntity

class MarkerRepo(private val markerDao:MarkerDao){
    suspend fun insertMarkerPos(marker: MarkerEntity){
        markerDao.insertMarkerPos(marker)
    }

    suspend fun getAllMarkers(): List<MarkerEntity>{
        return markerDao.getAllMarkers()
    }

}