package com.example.todoapp.viewmodels

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.todoapp.Notification.NotificationClass
import com.example.todoapp.repository.Repo
import com.example.todoapp.databases.WordRoomDatabase
import com.example.todoapp.databases.todolist
import com.example.todoapp.ui.UpdateTodo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.HashMap

class MainActivityModelView(application : Application): AndroidViewModel(application){
    var list_of_todo = MutableLiveData<MutableList<todolist>>()
    var repo = Repo()
    var app : Application = application
    var roomdb = WordRoomDatabase.getDatabase(application)

    lateinit var notification_creation : NotificationClass

    fun settingNotify(){
        notification_creation = NotificationClass(app)
        notification_creation.setChannel()
        notification_creation.fire_server_side()
    }


    fun readTodo(){
        viewModelScope.launch{
            var collection = roomdb.wordDao().getAllNotes()
            list_of_todo.postValue(collection)

        }
    }

    fun getTodos() : MutableLiveData<MutableList<todolist>>{
        return list_of_todo
    }

    fun addTodo(todo : todolist){
        viewModelScope.launch{
            roomdb.wordDao().insert(todo)
            readTodo()
        }
    }


    fun deleteTodo(todo : todolist){
        viewModelScope.launch {
            roomdb.wordDao().delete(todo)
            repo.deleteTodo("${todo.timehour}::${todo.minute}","${todo.year}-${todo.month}-${todo.day}")
            readTodo()
        }
    }


    fun updateRecord(hashmap : Intent, id : String){
        viewModelScope.launch {
            if(hashmap.hasExtra("name")){
                roomdb.wordDao().updateTodoName(hashmap.getStringExtra("name").toString(),id)
            }
            if(hashmap.hasExtra("date")){
                var s_date = hashmap.getStringExtra("date").toString()
                var day = s_date.split("-")[2]
                var month = s_date.split("-")[1]
                var year = s_date.split("-")[0]

                roomdb.wordDao().updateTodoDate(day,month,year,id)
            }
            if(hashmap.hasExtra("time")){
                var s_date = hashmap.getStringExtra("time").toString()
                var hour = s_date.split(":")[0]
                var minute = s_date.split(":")[1]
                roomdb.wordDao().updateTodoTime(hour,minute,id)
            }
            if(hashmap.hasExtra("notify")){
                roomdb.wordDao().updateTodoNotify(hashmap.getStringExtra("notify").toString(),id)
            }
            if(hashmap.hasExtra("desc")){
                roomdb.wordDao().updateTodoDescription(hashmap.getStringExtra("desc").toString(),id)
            }

            readTodo()
        }

    }

    fun addToFirebase(todo : todolist){
        viewModelScope.launch {
            repo.addTodo(todo)
        }
    }
}