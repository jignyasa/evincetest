package com.example.myapplication.viewmodel

import android.util.Log
import androidx.databinding.Observable
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
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
class MainActivityViewModel @Inject constructor(val repository: DataRepository) : ViewModel() {
    val _data=MutableLiveData<Pair<Any,Any>>()
    var list=ObservableArrayList<DataItem>()
    var isBackFromDetail=ObservableBoolean(false)
    /**
     * get data from remote
     */
    fun getDataApi(){
       CoroutineScope(Dispatchers.IO).launch {
           when(val loadedData= repository.getData()){
               is com.example.myapplication.model.Result.Success -> {
                   loadedData?.data?.let {
                       insertUserDataInDb(it) }
                   //_data.postValue(Pair(Constants.DATA_LOADED, loadedData))
               }
               is com.example.myapplication.model.Result.Error -> {
                   _data.postValue(Pair(Constants.ERROR, loadedData))
               }
           }
       }
    }

    /**
     * insert data in to db
     */
    fun insertUserDataInDb(data:ArrayList<DataItem>){
        CoroutineScope(Dispatchers.IO).launch {
            when(val loadedData= repository.insertUser(data)){
                is com.example.myapplication.model.Result.Success -> {
                }
                is com.example.myapplication.model.Result.Error -> {
                    Log.e("data inserted",loadedData.data.toString())
                    fetchUserDataInDb()
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