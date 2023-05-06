package com.example.myapplication.model

sealed class Result<T>(val data:T?,val message:String?) {
    class Success<T>(data: T) : com.example.myapplication.model.Result<T>(data,null){}
    class Error<T>(message: String?) : com.example.myapplication.model.Result<T>(null,message){}
}