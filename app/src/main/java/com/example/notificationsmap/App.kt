package com.example.notificationsmap

import android.app.Application
import android.content.Intent
import com.yandex.mapkit.MapKitFactory

class App: Application(){
    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(API_KEY)
        MapKitFactory.initialize(this)
        val intent = Intent(this, NotificationService::class.java)
        startService(intent)
    }
}