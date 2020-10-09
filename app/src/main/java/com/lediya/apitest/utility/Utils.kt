package com.lediya.apitest.utility

import android.content.Context
import android.net.ConnectivityManager

object Utils{
    /*
    Check the internet activity in the application **/
    fun  isConnectedToNetwork(context:Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return connectivityManager?.activeNetworkInfo?.isConnectedOrConnecting() ?: false
    }

}