package com.example.myapplication

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication : Application() {
    companion object{
        lateinit var appContext: Context
    }
    override fun onCreate() {
        appContext=applicationContext
        super.onCreate()
    }
}