package com.example.notificationsmap.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "markers")
data class Marker(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    @ColumnInfo(name = "latitude") val lat: Double,
    @ColumnInfo(name = "longitude") val lng: Double
) {
    companion object{
        fun from( lat: Double, lng: Double): Marker {
            return Marker(
                lat = lat,
                lng = lng
            )
        }
    }
}
