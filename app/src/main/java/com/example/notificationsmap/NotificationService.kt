package com.example.notificationsmap

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.notificationsmap.view.MainActivity

class NotificationService: Service() {
    private lateinit var notificationManager:NotificationManager

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel("NotificationServiceChannel","Notification Service Channel", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val notification = NotificationCompat.Builder(this,"NotificationServiceChannel")
            .setContentTitle("Сервис запущен!")
            .setContentText("Сервис напоминаний запущен!")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setSound(null)
            .build()

        startForeground(1,notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        notificationManager.cancel(1)
    }
}