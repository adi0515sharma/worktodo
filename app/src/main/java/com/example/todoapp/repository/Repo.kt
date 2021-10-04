package com.example.todoapp.repository

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.todoapp.Notification.NotificationClass
import com.example.todoapp.Notification.receiver.RegularAlarmReceiver
import com.example.todoapp.databases.WordRoomDatabase
import com.example.todoapp.databases.todolist
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.LocalTime
import java.util.*
import kotlin.collections.HashMap

class Repo {
    var db : DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users").child("task")

    fun addTodo(todo: todolist){

            GlobalScope.launch {

                db.child("${todo.year}-${todo.month}-${todo.day}").child("${todo.timehour}:${todo.minute}").setValue(todo).await()
                Log.e("Data Added", "Success")
            }

    }

    fun deleteTodo(time : String, date : String){
        GlobalScope.launch {
                db.child(date).child(time).removeValue().await()
        }
    }

    fun alterTodo(time : String, date : String , hashkey:HashMap<String, Any>){
        GlobalScope.launch {

            var documentReferese = db.child(date).child(time)
            for (k in hashkey) {
                documentReferese.setValue(k.key,k.value).await()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun fetching_in_background(context: Context){
        var date : LocalDate? = LocalDate.now()
        var roomdb = WordRoomDatabase.getDatabase(context.applicationContext)
        GlobalScope.launch{
                var documentReferese = db.child(date.toString()).get().await()

//                Log.e("it","${documentReferese.childrenCount}")
                if(documentReferese.hasChildren())
                {
                    documentReferese.children.forEach {
                        var color = it.child("color").getValue()
                        var day = it.child("day").getValue()
                        var description = it.child("description").getValue()
                        var minute = it.child("minute").getValue()
                        var month = it.child("month").getValue()

                        var notify = it.child("notify").getValue()
                        var timehour = it.child("timehour").getValue()
                        var todo_name = it.child("todo_name").getValue()
                        var year = it.child("year").getValue()
                        Log.e("task", "Success")


                        roomdb.wordDao().insert(todolist(
                            "${date}//${timehour}::${minute}", todo_name as String,
                            description as String,
                            notify as String,
                            color as String,
                            timehour as String,
                            minute as String,
                            day as String,
                            month as String,
                            year as String
                            )
                        )
                        regular_alert(timehour.toInt(), minute.toInt(), context)
                    }
                }
                else{
                    Log.e("message","no data found")
                }

            }
        }
//2021-9-23//5::10

    fun regular_alert(hour : Int, minute : Int, context: Context){
            Log.e("set alarm","set my alarm")
            val calendar: Calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
               // Log.e("hour","${Calendar.getInstance().get(Calendar.HOUR_OF_DAY)}")
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE,minute)
            }
            var alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            var alarmIntent = Intent(context as Activity, RegularAlarmReceiver::class.java).let { intent ->
                PendingIntent.getBroadcast(context as Activity, 0, intent, 0)
            }

            alarmMgr.setExact(AlarmManager.RTC_WAKEUP,calendar.timeInMillis, alarmIntent)
        //        alarmMgr?.setRepeating()
        }
}


