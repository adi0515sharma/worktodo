package com.example.todoapp.Notification.receiver
import android.R
import android.R.id.message
import android.app.Application
import android.app.Notification
import android.content.BroadcastReceiver
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.example.todoapp.databases.WordRoomDatabase
import com.example.todoapp.databases.todolist
import com.example.todoapp.repository.Repo
import com.example.todoapp.ui.MainActivity
import com.google.rpc.context.AttributeContext
import kotlinx.coroutines.*
import java.util.*


class RegularAlarmReceiver(): BroadcastReceiver() {

    var repo = Repo()
    lateinit var roomdb : WordRoomDatabase
    lateinit var todo : todolist

    companion object{
        var idkey = 1
        var hashMap : HashMap<String, Int> = HashMap<String, Int> ()
    }


    suspend fun fetchtask(hour : String, minute : String) {
        todo = roomdb.wordDao().getSpecificRecordBasedOnDateAndTime(hour, minute)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent?){
        Log.e("id", "${idkey}")
        roomdb = WordRoomDatabase.getDatabase(context.applicationContext)
        // Create an explicit intent for an Activity in your app
        GlobalScope.launch {
            fetchtask("${Calendar.getInstance().get(Calendar.HOUR_OF_DAY)}", "${Calendar.getInstance().get(Calendar.MINUTE)}")


            val builder = NotificationCompat.Builder(context, "alert_side")
                .setSmallIcon(R.drawable.btn_star)
                .setContentTitle(todo.todo_name)
                .setContentText(todo.description)
                .setColor(Color.BLUE)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setAutoCancel(true)

            val notificationManager = NotificationManagerCompat.from(context)
            RegularAlarmReceiver.hashMap.put("${todo.userId}",RegularAlarmReceiver.idkey)
            notificationManager.notify(RegularAlarmReceiver.idkey, builder.build())
            RegularAlarmReceiver.idkey+=1

        }


    }
}