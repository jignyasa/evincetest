package com.example.myapplication.repository

import com.example.myapplication.model.DataItem

interface DataRepository{
   suspend fun getData():com.example.myapplication.model.Result<ArrayList<DataItem>>

   suspend fun insertUser(dataItem: ArrayList<DataItem>):com.example.myapplication.model.Result<Long>

   suspend fun updateUser(dataItem: DataItem):com.example.myapplication.model.Result<Long>

   suspend fun deleteUser(dataItem: DataItem):com.example.myapplication.model.Result<Long>

   suspend fun fetchUser():com.example.myapplication.model.Result<ArrayList<DataItem>>

   suspend fun removeUser(data : DataItem):com.example.myapplication.model.Result<ArrayList<DataItem>>

   suspend fun editUser(data:DataItem):com.example.myapplication.model.Result<ArrayList<DataItem>>
}