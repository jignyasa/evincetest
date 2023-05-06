package com.example.myapplication.repository

import androidx.room.RoomDatabase
import com.example.myapplication.model.DataItem
import com.example.myapplication.model.db.UserDatabase
import com.example.myapplication.model.remote.RestApi
import javax.inject.Inject

class DataRepositoryImplementation @Inject constructor(val api: RestApi,val db:UserDatabase) : DataRepository {
    override suspend fun getData(): com.example.myapplication.model.Result<ArrayList<DataItem>> {
        return try {
            val response = api.getData()
            val result = response.body()
            if (response.isSuccessful) {
                var list=ArrayList<DataItem>()
                result?.data?.let {
                    list.addAll(result.data)
                }
                return com.example.myapplication.model.Result.Success(list)
            } else {
                return com.example.myapplication.model.Result.Error("there is no data")
            }
        } catch (e: Exception) {
            return com.example.myapplication.model.Result.Error(e.message.toString())
        }
    }

    override suspend fun editUser(data : DataItem): com.example.myapplication.model.Result<ArrayList<DataItem>> {
        return try {
            val response = api.editUser(data.id?:0,data.firstName?:"",data.email?:"")
            val result = response.body()
            if (response.isSuccessful) {
                var list=ArrayList<DataItem>()
                result?.data?.let {
                    list.addAll(result.data)
                }
                return com.example.myapplication.model.Result.Success(list)
            } else {
                return com.example.myapplication.model.Result.Error("there is no data")
            }
        } catch (e: Exception) {
            return com.example.myapplication.model.Result.Error(e.message.toString())
        }
    }

    override suspend fun removeUser(data : DataItem): com.example.myapplication.model.Result<ArrayList<DataItem>> {
        return try {
            val response = api.removeUser(data.id?:0)
            val result = response.body()
            if (response.isSuccessful) {
                var list=ArrayList<DataItem>()
                result?.data?.let {
                    list.addAll(result.data)
                }
                return com.example.myapplication.model.Result.Success(list)
            } else {
                return com.example.myapplication.model.Result.Error("there is no data")
            }
        } catch (e: Exception) {
            return com.example.myapplication.model.Result.Error(e.message.toString())
        }
    }

    override suspend fun insertUser(data:ArrayList<DataItem>): com.example.myapplication.model.Result<Long> {
        return try {
            val response = db.userDao().insertData(data)
            val result = response
            return com.example.myapplication.model.Result.Error(response.toString())
        } catch (e: Exception) {
            return com.example.myapplication.model.Result.Error(e.message.toString())
        }
    }

    override suspend fun updateUser(data:DataItem): com.example.myapplication.model.Result<Long> {
        return try {
            val response = db.userDao().updateData(data)
            val result = response
            return com.example.myapplication.model.Result.Error(response.toString())
        } catch (e: Exception) {
            return com.example.myapplication.model.Result.Error(e.message.toString())
        }
    }

    override suspend fun deleteUser(data:DataItem): com.example.myapplication.model.Result<Long> {
        return try {
            val response = db.userDao().deleteData(data)
            val result = response
            return com.example.myapplication.model.Result.Error(response.toString())
        } catch (e: Exception) {
            return com.example.myapplication.model.Result.Error(e.message.toString())
        }
    }

    override suspend fun fetchUser(): com.example.myapplication.model.Result<ArrayList<DataItem>> {
        return try {
            val response = db.userDao().fetchData()
            if (response.isEmpty().not()) {
                var list=ArrayList<DataItem>()
                response?.let {
                    list.addAll(response)
                }
                return com.example.myapplication.model.Result.Success(list)
            } else {
                return com.example.myapplication.model.Result.Error("there is no data")
            }
        } catch (e: Exception) {
            return com.example.myapplication.model.Result.Error(e.message.toString())
        }
    }
}