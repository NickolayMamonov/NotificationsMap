package com.example.notificationsmap.data.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ActiveTasks")
data class ActiveTaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long ?= null,
    @ColumnInfo(name = "isActive") val isActive: Boolean,
    @Embedded val marker : MarkerEntity
){
    companion object{
        fun add(isActive: Boolean,marker: MarkerEntity): ActiveTaskEntity{
            return  ActiveTaskEntity(
                isActive = isActive,
                marker = marker
            )

        }

    }
}
