package com.example.notificationsmap

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager

import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.example.notificationsmap.data.TaskRepo
import com.example.notificationsmap.data.database.MarkerDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.random.Random


class NotificationService: Service() {
    private lateinit var notificationManager:NotificationManager
    private lateinit var repo: TaskRepo
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.Main)
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        val markerDao = MarkerDatabase.getDatabase(application).markerDao()
        repo = TaskRepo(markerDao)
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if( ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.SET_ALARM
            ) != PackageManager.PERMISSION_GRANTED
        ){
            return START_NOT_STICKY
        }
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel = NotificationChannel("NotificationServiceChannel","Notification Service Channel", NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(notificationChannel)


        startForeground(1,buildNotification("Сервис запущен!", ""))

        val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationListener = LocationListener { location ->
            scope.launch{
                updateMarkerTasks(location)
            }
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,60000,0f,locationListener)

//        scope.launch{
//            val markers = repo.getAllTasks()
//            for (marker in markers){
//                createDateNotification(marker.name,marker.marker.desc,marker.marker.date,marker.marker.time)
//            }
//        }
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
                if(marker.isActive){
                    val markerX = marker.marker.lat
                    val markerY = marker.marker.lng
                    val circleX = lastKnownLocation.latitude
                    val circleY = lastKnownLocation.longitude
                    val isInside = isPointInside(markerX,markerY,circleX,circleY)
                    if (isInside) {
                        notificationManager.notify(2,buildNotification(marker.name,marker.marker.desc))
//                        Toast.makeText(this,"Попал", Toast.LENGTH_LONG).show()
                    }

                }

            }

        }
    }
    private fun buildNotification(title: String, text: String): Notification {

        return NotificationCompat.Builder(this, "NotificationServiceChannel")
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()
    }
    private fun isPointInside(
        markerX: Double,
        markerY: Double,
        circleX: Double,
        circleY: Double,
    ): Boolean {
        return (markerX - circleX) * (markerX - circleX) + (markerY - circleY) * (markerY - circleY) <= (0.0009000009 * 0.0009000009)

    }


//    private fun createDateNotification(name: String, desc: String, date: String, time: String) {
//
//        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val alarmIntent = Intent(this, NotificationReceiver::class.java).apply {
//            putExtra("title",name)
//            putExtra("desc",desc)
//        }
//
//        val pendingIntent = PendingIntent.getBroadcast(
//            this,
//            Random.nextInt(),
//            alarmIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT
//        )
//        val dateNot = date.split("/")
//        val timeNot = time.split(":")
//        val notificationTime = Calendar.getInstance()
//        notificationTime.set(Calendar.YEAR, dateNot[2].toInt())
//        notificationTime.set(Calendar.MONTH, dateNot[1].toInt()-1)
//        notificationTime.set(Calendar.DAY_OF_MONTH, dateNot[0].toInt())
//        notificationTime.set(Calendar.HOUR_OF_DAY, timeNot[1].toInt())
//        notificationTime.set(Calendar.MINUTE, timeNot[0].toInt())
//
//        alarmManager.setExactAndAllowWhileIdle(
//            AlarmManager.RTC_WAKEUP,
//            notificationTime.timeInMillis,
//            pendingIntent
//        )
//    }

}