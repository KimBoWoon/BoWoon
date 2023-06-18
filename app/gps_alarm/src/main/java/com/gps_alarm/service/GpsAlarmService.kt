package com.gps_alarm.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.IBinder
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.bowoon.android.gps_alarm.R
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.gps_alarm.data.Address
import com.gps_alarm.data.SettingInfo
import com.gps_alarm.ui.util.SendNotification
import com.gps_alarm.ui.util.parcelable
import com.gps_alarm.ui.util.parcelableArray
import dagger.hilt.android.AndroidEntryPoint
import util.Log
import javax.inject.Inject

@AndroidEntryPoint
class GpsAlarmService @Inject constructor() : Service() {
    companion object {
        const val ADDRESS_LIST = "addressList"
        const val SETTING_INFO = "settingInfo"
    }

    private var addressList: Array<Address>? = null
    private var settingInfo: SettingInfo? = null
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)

            val latitude = locationResult.lastLocation?.latitude ?: 0.0
            val longitude = locationResult.lastLocation?.longitude ?: 0.0

            val location = Location("").apply {
                this.latitude = latitude
                this.longitude = longitude
            }

            Log.d("latitude > $latitude, longitude > $longitude")

            addressList?.let { addressList ->
                addressList.filter { it.isEnable == true }.forEach { address ->
                    val destination = Location("").apply {
                        this.latitude = address.latitude ?: 0.0
                        this.longitude = address.longitude ?: 0.0
                    }
                    if (location.distanceTo(destination) <= (settingInfo?.circleSize ?: 0)) {
                        SendNotification(this@GpsAlarmService, 0)
                    }
                }
            }
        }
    }

    private fun hasPermission(): Boolean =
        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

    private fun createForegroundNotification(): NotificationCompat.Builder =
        NotificationCompat.Builder(applicationContext, resources.getString(R.string.notification_channel_id))
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentTitle("Location Service is Running")
            .setAutoCancel(false)

    @SuppressLint("MissingPermission")
    private fun startService() {
        if (hasPermission()) {
            LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(
                LocationRequest.create().apply {
                    interval = 4000
                    fastestInterval = 2000
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                },
                locationCallback,
                Looper.getMainLooper()
            )
            val notification = createForegroundNotification().build()
            startForeground(1, notification)
            (getSystemService(NOTIFICATION_SERVICE) as? NotificationManager)?.apply {
                notify("locationService", 0, notification)
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            it.action?.let { action ->
                if (action == "StartService") {
                    addressList = intent.extras?.parcelableArray(ADDRESS_LIST)
                    settingInfo = intent.extras?.parcelable(SETTING_INFO)
                    startService()
                } else {
                    removeLocationCallback()
                    stopForeground(true)
                    stopSelf()
                }
            }
        }
        return START_REDELIVER_INTENT
    }

    private fun removeLocationCallback() {
        LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}