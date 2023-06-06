package com.example.notificationsmap.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "markerstest")
data class MarkerEntity(
    @PrimaryKey(autoGenerate = true) val markid: Long? = 0,
    @ColumnInfo(name = "time") val time : String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "address") val address : String,
    @ColumnInfo(name = "description") val desc : String,
    @ColumnInfo(name = "latitude") val lat: Double,
    @ColumnInfo(name = "longitude") val lng: Double
)
{
    companion object{
        fun from( time: String,date: String,address: String,desc: String,lat: Double,lng: Double): MarkerEntity {
            return MarkerEntity(
                time = time,
                date = date,
                address = address,
                desc = desc,
                lat = lat,
                lng = lng
            )
        }
    }
}
