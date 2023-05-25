package com.example.notificationsmap.data.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Taskstest")
data class ActiveTaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long ?= null,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "isActive") val isActive: Boolean,
    @Embedded val marker : MarkerEntity
){
    companion object{
        fun add(name: String,isActive: Boolean,marker: MarkerEntity): ActiveTaskEntity{
            return  ActiveTaskEntity(
                name = name,
                isActive = isActive,
                marker = marker
            )

        }

    }
}
