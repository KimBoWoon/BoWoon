package com.bowoon.commonutils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build

fun Context.getActiveNetwork(): String? =
    (getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)?.let { connectivityManager ->
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            connectivityManager.activeNetworkInfo?.let {
                when (it.type) {
                    ConnectivityManager.TYPE_WIFI -> "WIFI"
                    ConnectivityManager.TYPE_MOBILE -> "CELLULAR"
                    ConnectivityManager.TYPE_ETHERNET -> "ETHERNET"
                    ConnectivityManager.TYPE_BLUETOOTH -> "BLUETOOTH"
                    ConnectivityManager.TYPE_VPN -> "VPN"
                    else -> null
                }
            }
        } else {
            connectivityManager.activeNetwork.let { network ->
                connectivityManager.getNetworkCapabilities(network)?.let { connection ->
                    when {
                        connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "WIFI"
                        connection.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "CELLULAR"
                        connection.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "ETHERNET"
                        connection.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> "BLUETOOTH"
                        connection.hasTransport(NetworkCapabilities.TRANSPORT_USB) -> "USB"
                        connection.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> "VPN"
                        else -> null
                    }
                }
            }
        }
    }

fun Context.getWifi(): Int {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        (getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)?.let { connectivityManager ->
            val request = NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .build()
            val networkCallback = object : ConnectivityManager.NetworkCallback(FLAG_INCLUDE_LOCATION_INFO) {
                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    when {
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> networkCapabilities.transportInfo as? WifiInfo
                        else -> (applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager)?.connectionInfo
                    }?.let { wifiInfo ->
                        val ssid = wifiInfo.ssid
                        val level = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            (applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager)?.calculateSignalLevel(wifiInfo.rssi)
                        } else {
                            WifiManager.calculateSignalLevel(wifiInfo.rssi, 5)
                        }

                        Log.d("rss_reader_get_wifi", "ssid > $ssid, level > $level")
                    }
                }
            }
            connectivityManager.registerNetworkCallback(request, networkCallback)
        }
    } else {
        (applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager)?.let { wifiManager ->
            val ssid = wifiManager.connectionInfo.ssid
            val level = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                wifiManager.calculateSignalLevel(wifiManager.connectionInfo.rssi)
            } else {
                WifiManager.calculateSignalLevel(wifiManager.connectionInfo.rssi, 5)
            }

            Log.d("rss_reader_get_wifi", "ssid > $ssid, level > $level")
        }
    }
    return 1
}