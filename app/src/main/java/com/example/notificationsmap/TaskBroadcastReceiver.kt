package com.example.notificationsmap

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

class TaskBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannelId= "geofence_channel"
        val channel = NotificationChannel(
            notificationChannelId,
            "Geofence Notifications",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)
        val notificationBuilder = NotificationCompat.Builder(context, notificationChannelId)
            .setContentTitle("Geofence Triggered")
            .setContentText("You have entered a geofenced area")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
        notificationManager.notify(0, notificationBuilder.build())

    }
}