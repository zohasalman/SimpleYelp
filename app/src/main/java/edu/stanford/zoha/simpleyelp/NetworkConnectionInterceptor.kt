package edu.stanford.zoha.simpleyelp

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import edu.stanford.zoha.simpleyelp.MainActivity as MainActivity


class NetworkConnectionInterceptor(val context: Context){

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    public fun isConnectionOn() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val connection = connectivityManager.getNetworkCapabilities(network)
            if (connection!=null){
                when {
                    connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        Toast.makeText(context, "Wifi Enabled", Toast.LENGTH_SHORT).show();
                    }
                    connection.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        Toast.makeText(context, "Data Network Enabled", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            else {
                Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
            else {
            val activeNetwork = connectivityManager.activeNetworkInfo
            if (activeNetwork != null) {
                when (activeNetwork.type) {
                    ConnectivityManager.TYPE_WIFI -> {
                        Toast.makeText(context, "Wifi Enabled", Toast.LENGTH_SHORT).show();
                    }
                    ConnectivityManager.TYPE_MOBILE -> {
                        Toast.makeText(context, "Data Network Enabled", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            else {
                Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }}
    }}