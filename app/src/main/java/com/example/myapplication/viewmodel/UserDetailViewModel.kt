package com.example.myapplication.viewmodel

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.DataItem
import com.example.myapplication.repository.DataRepository
import com.example.myapplication.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(val repository: DataRepository) : ViewModel() {
    val _data=MutableLiveData<Pair<Any,Any>>()
    var list=ObservableArrayList<DataItem>()
    var name=ObservableField<String>()
    var email=ObservableField<String>()

    /**
     * get data from remote
     */
    fun removeUser(data:DataItem){
       CoroutineScope(Dispatchers.IO).launch {
           when(val loadedData= repository.removeUser(data)){
               is com.example.myapplication.model.Result.Success -> {
                   Log.e(UserDetailViewModel::class.java.simpleName,loadedData.data.toString())
               }
               is com.example.myapplication.model.Result.Error -> {
                   removeData(data)
                   _data.postValue(Pair(Constants.ERROR, loadedData))
               }
           }
       }
    }

    /**
     *remove data from db
     */
    fun removeData(data:DataItem){
       CoroutineScope(Dispatchers.IO).launch {
           when(val loadedData= repository.deleteUser(data)){
               is com.example.myapplication.model.Result.Success -> {
                   _data.postValue(Pair(Constants.UPDATED_DATA, loadedData))
                   Log.e(UserDetailViewModel::class.java.simpleName,loadedData.data.toString())
               }
               is com.example.myapplication.model.Result.Error -> {
                   _data.postValue(Pair(Constants.UPDATED_DATA, loadedData))
               }
           }
       }
    }


    /**
     *update data in db
     */
    fun updateData(data:DataItem){
       CoroutineScope(Dispatchers.IO).launch {
           when(val loadedData= repository.updateUser(data)){
               is com.example.myapplication.model.Result.Success -> {
                   Log.e(UserDetailViewModel::class.java.simpleName,loadedData.data.toString())
               }
               is com.example.myapplication.model.Result.Error -> {
                   _data.postValue(Pair(Constants.UPDATED_DATA, loadedData))
               }
           }
       }
    }

    /**
     * get data from remote
     */
    fun editUser(data:DataItem){
       CoroutineScope(Dispatchers.IO).launch {
           when(val loadedData= repository.updateUser(data)){
               is com.example.myapplication.model.Result.Success -> {
                   Log.e(UserDetailViewModel::class.java.simpleName,loadedData.data.toString())
               }
               is com.example.myapplication.model.Result.Error -> {
                   updateData(data)
               }
           }
       }
    }


    /**
     * fetch data from db
     */
    fun fetchUserDataInDb(){
        CoroutineScope(Dispatchers.IO).launch {
            when(val loadedData= repository.fetchUser()){
                is com.example.myapplication.model.Result.Success -> {
                   Log.e("data fetched",loadedData.data.toString())
                    loadedData.data?.let {
                        list.clear()
                        list.addAll(loadedData.data)
                        _data.postValue(Pair(Constants.DATA_LOADED,loadedData.data))
                    }
                }
                is com.example.myapplication.model.Result.Error ->_data.postValue(Pair("ERROR",loadedData))
            }
        }
    }

}