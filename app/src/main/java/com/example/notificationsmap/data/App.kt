package com.example.notificationsmap.data

import android.app.Application
import androidx.room.Room
import com.example.notificationsmap.data.database.MarkerDatabase

class App: Application() {
    private lateinit var db: MarkerDatabase

    override fun onCreate() {
        super.onCreate()

        db = Room.databaseBuilder(
            this,
            MarkerDatabase::class.java, "markers"
        ).build()
    }

    fun getDb(): MarkerDatabase {
        return db
    }

}