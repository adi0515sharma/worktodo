package com.example.todoapp.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.widget.*
import com.example.todoapp.R
import java.util.*

class UpdateTodo : AppCompatActivity() , DatePickerDialog.OnDateSetListener,
TimePickerDialog.OnTimeSetListener{


    lateinit var todo_name : EditText
    lateinit var date : TextView
    lateinit var update : Button
    lateinit var clear : Button
    lateinit var time : TextView
    lateinit var switch : Switch
    lateinit var desc : EditText
    lateinit var id : TextView
    lateinit var switchBoolean : String

    var day : Int = 0
    var month : Int = 0
    var year : Int = 0
    var hour : Int = 0
    var minute : Int = 0

    var task_id : String = ""
    var task_name : String = ""
    var task_date : String = ""
    var task_time : String = ""
    var task_desc : String = ""
    var task_notifiy : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_todo)
        task_id =intent.getStringExtra("userid") as String
        task_name = intent.getStringExtra("name") as String
        task_date = intent.getStringExtra("date") as String
        task_time = intent.getStringExtra("time") as String
        task_desc = intent.getStringExtra("desc") as String
        task_notifiy = intent.getStringExtra("notifiy") as String

        id = findViewById(R.id.todo_id)
        todo_name =  findViewById<EditText>(R.id.todo_update_set_name)
        update = findViewById(R.id.submit)
        clear = findViewById(R.id.clear)
        date = findViewById(R.id.todo_update_set_date)
        time = findViewById(R.id.todo_update_set_time)
        desc = findViewById<EditText>(R.id.todo_update_set_desc)
        switch = findViewById<Switch>(R.id.switch_update)

        id.text = "Task Id : ${task_id}"
        todo_name.setText(task_name)
        date.text = task_date
        time.text = task_time
        desc.setText(task_desc)

        if(task_notifiy.equals("On")){

        }
        else{

        }

        switch.setOnCheckedChangeListener { _, isChecked ->
            switchBoolean = if(isChecked){
                Toast.makeText(this@UpdateTodo,"You Will Get Notified",Toast.LENGTH_LONG).show()
                "On"
            }
            else{
                Toast.makeText(this@UpdateTodo,"You Will Not Get Notified",Toast.LENGTH_LONG).show()
                "Off"
            }

            Log.e("switch is ",switchBoolean)
        }
        date.setOnClickListener {
            var c : Calendar = Calendar.getInstance()
            day = c.get(Calendar.DAY_OF_MONTH)
            month = c.get(Calendar.MONTH)
            year = c.get(Calendar.YEAR)
            val datePickerDialog =
                DatePickerDialog(this@UpdateTodo, this@UpdateTodo, year, month,day)
            datePickerDialog.show()
        }

        time.setOnClickListener{
            val calendar: Calendar = Calendar.getInstance()
            var hour = calendar.get(Calendar.HOUR)
            var minute = calendar.get(Calendar.MINUTE)
            val timePickerDialog = TimePickerDialog(this@UpdateTodo, this@UpdateTodo, hour, minute,
                DateFormat.is24HourFormat(this))
            timePickerDialog.show()
        }

        update.setOnClickListener {
            Submit_to_database()
        }

    }


    fun Submit_to_database(){
        // update to firebase
        // update to table
        var hashMap : HashMap<String, String> = HashMap<String, String> ()
        var intent = Intent();
        if(!task_name.equals(todo_name.text.toString())){
            intent.putExtra("name",todo_name.text.toString())
        }
        if(!task_date.equals(date.text.toString())){
            intent.putExtra("date",date.text.toString())
        }
        if(!task_time.equals(time.text.toString())){
            intent.putExtra("time",time.text.toString())
        }
        if(!task_desc.equals(desc.text.toString())){
            intent.putExtra("desc",desc.text.toString())
        }
        if(!task_notifiy.equals(switchBoolean)){
            intent.putExtra("notify",switchBoolean)
        }
        setResult(54,intent)
        finish()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        this.year = year
        this.month = month
        this.day = dayOfMonth
        date.text = "${year}-${month}-${day}"
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        this.hour = hourOfDay
        this.minute = minute
        time.text = "${hour}:${minute}"

    }
}