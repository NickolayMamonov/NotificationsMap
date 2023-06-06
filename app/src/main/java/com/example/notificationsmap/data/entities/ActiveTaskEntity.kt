package com.example.notificationsmap.data.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.math.ln

@Entity(tableName = "Taskstest")
data class ActiveTaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "address") val address : String,
    @ColumnInfo(name = "description") val desc : String,
    @ColumnInfo(name = "latitude") val lat: Double,
    @ColumnInfo(name = "longitude") val lng: Double
){
    companion object{
        fun add(name: String,address: String,desc:String,lat:Double,lng:Double): ActiveTaskEntity{
            return  ActiveTaskEntity(
                name = name,
                address = address,
                desc = desc,
                lat = lat,
                lng = lng
            )

        }

    }
}
