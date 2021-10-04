package com.example.todoapp.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.todoapp.R
import java.util.*

class TodoDisplay : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    lateinit var todo_name : EditText
    lateinit var date : TextView
    lateinit var submit : Button
    lateinit var clear : Button
    lateinit var time : TextView
    lateinit var switch : Switch
    var day : Int = 0
    var month : Int = 0
    var year : Int = 0
    var hour : Int = 0
    var minute : Int = 0

    lateinit var selected_date : String
    lateinit var selected_time : String
    lateinit var desc : EditText
    lateinit var switchBoolean : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        setContentView(R.layout.content_todo_display)

        todo_name =  findViewById(R.id.todo_set_name)
        date = findViewById(R.id.todo_set_date)
        submit = findViewById(R.id.submit)
        clear = findViewById(R.id.update)
        date = findViewById(R.id.todo_set_date)
        time = findViewById(R.id.todo_set_time)
        desc = findViewById(R.id.todo_set_desc)
        switch = findViewById(R.id.switch_create)

        switch.setOnCheckedChangeListener { _, isChecked ->
            switchBoolean = if(isChecked){
                Toast.makeText(this@TodoDisplay,"You Will Get Notified",Toast.LENGTH_LONG).show()
                "On"
            }
            else{
                Toast.makeText(this@TodoDisplay,"You Will Not Get Notified",Toast.LENGTH_LONG).show()
                "Off"
            }
        }
        date.setOnClickListener {
            var c : Calendar = Calendar.getInstance()
            day = c.get(Calendar.DAY_OF_MONTH)
            month = c.get(Calendar.MONTH)
            year = c.get(Calendar.YEAR)
            val datePickerDialog =
                DatePickerDialog(this@TodoDisplay, this@TodoDisplay, year, month,day)
            datePickerDialog.show()
        }

        time.setOnClickListener{
            val calendar: Calendar = Calendar.getInstance()
            var hour = calendar.get(Calendar.HOUR)
            var minute = calendar.get(Calendar.MINUTE)
            val timePickerDialog = TimePickerDialog(this@TodoDisplay, this@TodoDisplay, hour, minute,
                DateFormat.is24HourFormat(this))
            timePickerDialog.show()
        }

        submit.setOnClickListener{
            Submit_to_database(todo_name.text.toString())
        };

        clear.setOnClickListener {
            todo_name.text.clear()
        }
    }


    fun Submit_to_database(name : String){
        // submit to firebase
        var intent = Intent();

        if(name.equals("")){
            Toast.makeText(this,"Please enter the todo name",Toast.LENGTH_LONG).show()
        }
        else if(date.equals("")){
            Toast.makeText(this,"Please enter the todo date",Toast.LENGTH_LONG).show()
        }
        else{

            Log.e("name",name)
            Log.e("desc",desc.text.toString())
            intent.putExtra("Todo",name)

            intent.putExtra("year",this.year)
            intent.putExtra("month",this.month)
            intent.putExtra("dayofmonth",this.day)

            intent.putExtra("hour",this.hour)
            intent.putExtra("minute",this.minute)

            intent.putExtra("description",desc.text.toString())
            intent.putExtra("switch",switchBoolean)

        }
        setResult(45,intent);
        finish();
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