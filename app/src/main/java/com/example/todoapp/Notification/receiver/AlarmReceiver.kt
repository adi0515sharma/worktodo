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
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class AlarmReceiver(): BroadcastReceiver() {

    var repo = Repo()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent?){

        // Create an explicit intent for an Activity in your app


        val builder = NotificationCompat.Builder(context, "server_side")
            .setSmallIcon(R.drawable.btn_star)
            .setContentTitle("Fetching Today's Task  ..... ")
            .setContentText("Server side")
            .setColor(Color.BLUE)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that will fire when the user taps the notification
            .setAutoCancel(true)
        val notificationManager = NotificationManagerCompat.from(context)

        notificationManager.notify(1, builder.build())

        repo.fetching_in_background(context)
    }
}