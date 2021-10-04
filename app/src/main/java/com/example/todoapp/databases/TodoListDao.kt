package com.example.todoapp.databases

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface TodoListDao {
    /**
     *  Below Code Is for insert
     */
    @Insert
    suspend fun insert(note: todolist)

    /**
     *  Below Code Is for delete
     */

    @Delete  // delete specific record
    suspend fun delete(note: todolist)


    @Query("delete from todolist") // delete all node
    suspend fun deleteAllNotes()



    /**
     *  Below Code Is for fetch
     */
    @Query("select * from todolist")
    suspend fun getAllNotes(): MutableList<todolist>
    @Query("select * from todolist WHERE userId = :id")
    suspend fun getSpecificRecord(id : Int): todolist
    @Query("select * from todolist WHERE hour = :hour AND minute = :minute")
    suspend fun getSpecificRecordBasedOnDateAndTime(hour : String, minute : String): todolist

    /**
     *  Below Code Is for update
     */
    @Query("UPDATE todolist SET todoName = :t_n WHERE userId = :id")
    suspend fun updateTodoName(t_n : String, id : String)

    @Query("UPDATE todolist SET description = :t_n WHERE userId = :id")
    suspend fun updateTodoDescription(t_n : String, id : String)


    @Query("UPDATE todolist SET notify = :t_n WHERE userId = :id")
    suspend fun updateTodoNotify(t_n : String, id : String)

    @Query("UPDATE todolist SET hour = :t_n AND minute = :m_n  WHERE userId = :id")
    suspend fun updateTodoTime(t_n : String, m_n : String , id : String)

    @Query("UPDATE todolist SET day = :d_n AND month = :m_n AND year = :y_n WHERE userId = :id")
    suspend fun updateTodoDate(d_n : String, m_n : String , y_n : String , id : String)

}