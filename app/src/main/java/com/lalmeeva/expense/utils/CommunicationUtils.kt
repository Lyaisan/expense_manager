package com.lalmeeva.expense.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

class CommunicationUtils {
    companion object {

        /** Response Timeout default, milliseconds  */
        const val RESPONSE_TIMEOUT = 180000   // 180 seconds
        /** Connection Timeout default, milliseconds  */
        const val CONNECTION_TIMEOUT = 20000   // 20 seconds
        /** Default Server Url  */
        const val SERVER_URL = "http://192.168.0.2"
        /** Default Server Port  */
        const val SERVER_PORT = 8000

        fun getConnectionType(context: Context): Int {
            var result = 0 // Returns connection type. 0: none; 1: mobile data; 2: wifi
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cm?.run {
                    cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                        if (hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                            result = 2
                        } else if (hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                            result = 1
                        }
                    }
                }
            } else {
                cm?.run {
                    cm.activeNetworkInfo?.run {
                        if (type == ConnectivityManager.TYPE_WIFI) {
                            result = 2
                        } else if (type == ConnectivityManager.TYPE_MOBILE) {
                            result = 1
                        }
                    }
                }
            }
            return result
        }
    }
}