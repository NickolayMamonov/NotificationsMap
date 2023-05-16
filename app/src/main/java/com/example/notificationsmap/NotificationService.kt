package com.example.notificationsmap

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.notificationsmap.data.database.MarkerDatabase
import com.example.notificationsmap.view.MainActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NotificationService : Service() {
    private lateinit var database:MarkerDatabase
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        database = MarkerDatabase.getDatabase(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // получаем данные из базы данных

        GlobalScope.launch {
            val tasks = database.markerDao().getAllTasks()
            for (task in tasks) {
                // создаем уведомление на основе данных из базы данных
                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channel = NotificationChannel(
                        "channel_id",
                        "channel_name",
                        NotificationManager.IMPORTANCE_HIGH
                    )
                    notificationManager.createNotificationChannel(channel)
                }

                val notificationIntent = Intent(this@NotificationService, MainActivity::class.java)
                val pendingIntent = PendingIntent.getActivity(
                    this@NotificationService,
                    0,
                    notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

                val notification = NotificationCompat.Builder(this@NotificationService, "channel_id")
                    .setContentTitle(task.name)
                    .setContentText(task.marker.address)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build()

                task.id?.let { notificationManager.notify(it.toInt(), notification) }
            }

            // останавливаем сервис
            stopSelf()
        }
        return START_NOT_STICKY
    }
}