package com.example.myapplication.model.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.model.DataItem

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(data:ArrayList<DataItem>)

    @Update
    suspend fun updateData(data:DataItem)

    @Delete
    suspend fun deleteData(data:DataItem)

    @Query("SELECT * FROM userTable")
    fun fetchData():List<DataItem>
}