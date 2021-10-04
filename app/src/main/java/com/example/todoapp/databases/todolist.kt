package com.example.todoapp.databases

import androidx.room.*

@Entity(tableName = "todolist")
class todolist(

    @PrimaryKey
    var userId: String,

    @ColumnInfo(name = "todoName")
    var todo_name: String,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "notify")
    var notify: String,

    @ColumnInfo(name = "color")
    var color: String,

    @ColumnInfo(name = "hour")
    var timehour: String,

    @ColumnInfo(name = "minute")
    var minute: String,

    @ColumnInfo(name = "day")
    var day: String,

    @ColumnInfo(name = "month")
    var month: String,

    @ColumnInfo(name = "year")
    var year: String
    )