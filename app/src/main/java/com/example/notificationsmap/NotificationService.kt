package com.example.notificationsmap

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.example.notificationsmap.data.TaskRepo

import com.example.notificationsmap.data.database.MarkerDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class NotificationService: Service() {
    private lateinit var notificationManager:NotificationManager
    private lateinit var repo: TaskRepo
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        val markerDao = MarkerDatabase.getDatabase(application).markerDao()
        repo = TaskRepo(markerDao)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel("NotificationServiceChannel","Notification Service Channel", NotificationManager.IMPORTANCE_DEFAULT)
            val notificationChannel2 = NotificationChannel("NotificationServiceChannel2","Notification Service Channel2", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(notificationChannel)
            notificationManager.createNotificationChannel(notificationChannel2)
        }


        startForeground(1,buildNotification("Сервис запущен!", ""))


    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED

        val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val executor = Executors.newSingleThreadScheduledExecutor()
        val locationRunnable = Runnable {
            CoroutineScope(Dispatchers.Main).launch{
                val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                updateMarkerTasks(lastKnownLocation)
            }
        }
        executor.scheduleAtFixedRate(locationRunnable, 0, 1, TimeUnit.MINUTES)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        notificationManager.cancel(1)
    }
    private suspend fun updateMarkerTasks(lastKnownLocation: Location?) {
        val markers = repo.getAllTasks()
        if(lastKnownLocation != null){
            for (marker in markers) {
                val markerX = marker.marker.lat
                val markerY = marker.marker.lng
                val circleX = lastKnownLocation.latitude
                val circleY = lastKnownLocation.longitude
                val isInside = isPointInside(markerX,markerY,circleX,circleY)
                if (isInside) {
                    notificationManager.notify(2,buildNotification("Текст","Описание"))
                    Toast.makeText(this,"Попал", Toast.LENGTH_LONG).show()
                }
            }

        }
    }
    private fun buildNotification(title: String, text: String): Notification {

        val notification = NotificationCompat.Builder(this, "NotificationServiceChannel")
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        return notification
    }
    private fun isPointInside(
        markerX: Double,
        markerY: Double,
        circleX: Double,
        circleY: Double,
    ): Boolean {
        if ((markerX - circleX) * (markerX - circleX) + (markerY - circleY) * (markerY - circleY) <= 0.0009000009){
            return true
        } else {
            return false
        }

    }


}