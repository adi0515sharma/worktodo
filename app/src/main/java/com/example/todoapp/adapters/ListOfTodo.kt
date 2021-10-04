package com.example.todoapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.databases.todolist

class ListOfTodo(var context : Context, private var mList: MutableList<todolist>
,private val listener: OnItemClickListener
)
    : RecyclerView.Adapter<ListOfTodo.ViewHolder>(){



    private var mListener : OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item, parent, false)
        mListener = listener
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title?.text = mList[position].todo_name
        var pmoram : String
        var time : String = ""
        if(mList[position].timehour.toInt() > 12){
            time = "Today at ${mList[position].timehour.toInt() % 12}:${mList[position].minute}PM"
        }
        else{
            time = "Today at ${mList[position].timehour}:${mList[position].minute}AM"
        }
        holder.time?.text = time
    }


    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder (view: View) : RecyclerView.ViewHolder(view){
        var title: TextView ? = view.findViewById<TextView>(R.id.todo_name)
        var time: TextView ? = view.findViewById<TextView>(R.id.time)
//\\        var update: ImageView ? = view.findViewById<ImageButton>(R.id.setagain)
        var delete: ImageView ? = view.findViewById<ImageButton>(R.id.delete)
        var second_linear: LinearLayout ?= view.findViewById<LinearLayout>(R.id.second_linear_layout)
        init {
            delete?.setOnClickListener {
                var position : Int = bindingAdapterPosition
                if(position != RecyclerView.NO_POSITION) {
                    mListener?.onDeleteClick(view, position)
                }
            }
//            update?.setOnClickListener {
//                var position : Int = bindingAdapterPosition
//                if(position != RecyclerView.NO_POSITION){
//                    mListener?.onUpdateClick(view, position)
//                }
//            }
            view.setOnClickListener {

                var position : Int = bindingAdapterPosition
                if(position != RecyclerView.NO_POSITION){
                    mListener?.onItemClick(view, position)
                }
            }
        }


    }

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
        fun onDeleteClick(view: View, position: Int)
        fun onUpdateClick(view: View, position: Int)
    }

    fun updateAdapter(list : MutableList<todolist>){
        mList = list
        notifyDataSetChanged()
    }


}