package com.example.notificationsmap.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "markerpoints")
data class MarkerEntity(
    @PrimaryKey(autoGenerate = true) val markid: Long? = null,
    @ColumnInfo(name = "time_arrival") val time_arrival : String,
    @ColumnInfo(name = "date_arrival") val date_arrival: String,
    @ColumnInfo(name = "time_remember") val time_remember : String,
    @ColumnInfo(name = "date_remember") val date_remember : String,
    @ColumnInfo(name = "address") val address : String,
    @ColumnInfo(name = "description") val desc : String,
    @ColumnInfo(name = "latitude") val lat: Double,
    @ColumnInfo(name = "longitude") val lng: Double
) {
    companion object{
        fun from( time_arrival: String,date_arrival: String,time_remember: String,date_remember: String,address: String,desc: String,lat: Double,lng: Double): MarkerEntity {
            return MarkerEntity(
                time_arrival = time_arrival,
                date_arrival = date_arrival,
                time_remember = time_remember,
                date_remember = date_remember,
                address = address,
                desc = desc,
                lat = lat,
                lng = lng
            )
        }
    }
}
