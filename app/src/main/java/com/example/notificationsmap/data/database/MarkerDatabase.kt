package com.example.notificationsmap.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


import com.example.notificationsmap.data.entities.Marker


@Database(entities = [Marker::class], version = 1)
abstract class MarkerDatabase: RoomDatabase() {
    abstract fun mapDao(): MarkerDao

    companion object{

        private lateinit var db: MarkerDatabase

        fun getDatabase(context: Context): MarkerDatabase {
            db = Room.databaseBuilder(
                context,
                MarkerDatabase::class.java, "markers"
            ).build()
            return db
        }
    }
}