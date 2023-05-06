package com.example.myapplication.util

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.os.Build
import android.util.Log

class Utility {
    companion object{
         fun isOnline(context: Context): Boolean {
            val connManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities = connManager.getNetworkCapabilities(connManager.activeNetwork)
                if (networkCapabilities == null) {
                    return false
                } else {
                    return true
                }
            } else {
                // below Marshmallow
                val activeNetwork = connManager.activeNetworkInfo
                if (activeNetwork?.isConnectedOrConnecting == true && activeNetwork.isAvailable) {
                    return true
                } else {
                    return false
                }
            }
        }
    }
}