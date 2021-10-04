package com.example.todoapp.Notification

import android.R
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.todoapp.Notification.receiver.AlarmReceiver
import com.example.todoapp.Notification.receiver.RegularAlarmReceiver
import java.util.*

// TODO: 24-09-2021 iniciate the class when user login
class NotificationClass(app : Application){


    lateinit var notificationManager : NotificationManager
    lateinit var server_ntf_channel : NotificationChannel
    lateinit var alert_ntf_channel : NotificationChannel
    var application : Application = app

    fun setChannel(){
        notificationManager = this.application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        serverChannel()
        alertChannel()
    }

    private fun serverChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val importance = NotificationManager.IMPORTANCE_DEFAULT
            server_ntf_channel = NotificationChannel("server_side", "Server Side", importance)
            notificationManager.createNotificationChannel(server_ntf_channel)
        }
    }
    private fun alertChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val importance = NotificationManager.IMPORTANCE_DEFAULT
            alert_ntf_channel = NotificationChannel("alert_side", "Alert Side", importance)
            notificationManager.createNotificationChannel(alert_ntf_channel)
        }
    }


    fun fire_server_side(){
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            Log.e("hour","${Calendar.getInstance().get(Calendar.HOUR_OF_DAY)}")
            set(Calendar.HOUR_OF_DAY, 20)
            set(Calendar.MINUTE,55)
        }
        var alarmMgr = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        var alarmIntent = Intent(application, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(application, 0, intent, 0)
        }

        alarmMgr?.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            alarmIntent
        )
//        alarmMgr?.setRepeating()
    }



}