package com.example.todoapp.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.adapters.ListOfTodo
import com.example.todoapp.databases.todolist
import com.example.todoapp.viewmodels.MainActivityModelView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDate
import java.util.*
import kotlin.properties.Delegates


class MainActivity : AppCompatActivity(){

    lateinit var model : MainActivityModelView;
    lateinit var add_todo : FloatingActionButton
    lateinit var recycle_of_list : RecyclerView
    lateinit var adapter: ListOfTodo
    var todo_for_today : MutableList<todolist> = mutableListOf<todolist>()
    lateinit var task : todolist
    var millis by Delegates.notNull<Long>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        add_todo = findViewById(R.id.add_new_todo)
        model = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))
            .get(MainActivityModelView::class.java)
        model.settingNotify()

        recycle_of_list = findViewById(R.id.recycle_view_of_items)
        adapter = ListOfTodo(this,todo_for_today,object : ListOfTodo.OnItemClickListener{

            override fun onItemClick(view: View, position: Int) {
                var intent : Intent = Intent(this@MainActivity,UpdateTodo::class.java)
                task = todo_for_today.get(position)
                intent.putExtra("userid",task.userId)
                intent.putExtra("name",task.todo_name)
                intent.putExtra("date","${task.year}-${task.month}-${task.day}")
                intent.putExtra("time","${task.timehour}::${task.minute}")
                intent.putExtra("desc",task.description)
                intent.putExtra("notifiy",task.notify)
                startActivityForResult(intent,54)
            }
            override fun onDeleteClick(view: View, position: Int) {
                model.deleteTodo(todo_for_today[position])
            }

            override fun onUpdateClick(view: View, position: Int) {

            }

        })
        recycle_of_list.adapter = adapter
        recycle_of_list.layoutManager = LinearLayoutManager(this)
        recycle_of_list.setHasFixedSize(true)

        var count:LiveData<MutableList<todolist>> = model.getTodos()
        count.observe(this, {
            todo_for_today = it
            Log.e("values","${todo_for_today.size}")
            adapter.updateAdapter(todo_for_today)
        })
        add_todo.setOnClickListener {
            val intent = Intent(this, TodoDisplay::class.java)
            startActivityForResult(intent,45)
        }
        model.readTodo()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 45){
            if (data != null) {
                if(data.hasExtra("Todo")){

                    millis = System.currentTimeMillis();
                    var date = data.getIntExtra("dayofmonth",0).toString()
                    var month = (data.getIntExtra("month",0)+1).toString()
                    var year = data.getIntExtra("year",0).toString()
                    var mintue = data.getIntExtra("minute",0).toString()
                    var hour = data.getIntExtra("hour",0).toString()

                    model.addToFirebase(
                            todolist(
                                userId = "${year}-${month}-${date}//${hour}::${mintue}",
                                todo_name = data.getStringExtra("Todo").toString(),
                                description = data.getStringExtra("description").toString(),

                                timehour = hour,
                                minute = mintue,

                                day = date,
                                month = month,
                                year = year,

                                notify = data.getStringExtra("switch").toString(),
                                color = "Red"
                            )
                    )
                    if(java.time.LocalDate.now().toString().replace("0","").equals("${year}-${month}-${date}".replace("0","")))
                        {
                            // save it to room database
                            model.addTodo(todolist(
                                userId = "${year}-${month}-${date}//${hour}::${mintue}",
                                todo_name = data.getStringExtra("Todo").toString(),
                                description = data.getStringExtra("description").toString(),

                                timehour = hour,
                                minute = mintue,

                                day = date,
                                month = month,
                                year = year,

                                notify = data.getStringExtra("switch").toString(),
                                color = "Red"
                            ))

                            model.repo.regular_alert(hour.toInt(), mintue.toInt(), this)

                        }

                    }
                }
            }

    }
}
