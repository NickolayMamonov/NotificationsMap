package com.example.notificationsmap

import android.app.Application
import com.yandex.mapkit.MapKitFactory

class App: Application(){

    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(API_KEY)
        MapKitFactory.initialize(this)
    }
}