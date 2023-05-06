package com.example.myapplication.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.model.DataItem

@Database(entities = [DataItem::class], version = 1, exportSchema = false)
abstract class UserDatabase:RoomDatabase() {

    abstract fun userDao():UserDao

    companion object{
        var dbInstance:UserDatabase?=null
        fun getInstance(context:Context):UserDatabase{
            synchronized(UserDatabase::class.java){
                if(dbInstance==null){
                    dbInstance=Room.databaseBuilder(context,UserDatabase::class.java,"my_db").fallbackToDestructiveMigration().build()
                }
            }
            return dbInstance!!
        }

    }
}