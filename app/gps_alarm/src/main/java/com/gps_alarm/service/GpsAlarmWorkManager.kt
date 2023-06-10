package com.gps_alarm.service

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
import android.location.Location
import android.os.Build
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.bowoon.android.gps_alarm.R
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.gps_alarm.data.Address
import com.gps_alarm.ui.util.SendNotification
import com.gps_alarm.ui.util.decode
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.serialization.json.Json
import util.Log

@HiltWorker
class GpsAlarmWorkManager @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted parameters: WorkerParameters,
) : CoroutineWorker(context, parameters) {
    private val notificationId = 0
    private var addressList: Array<Address>? = null
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)

            latitude = locationResult.lastLocation?.latitude ?: 0.0
            longitude = locationResult.lastLocation?.longitude ?: 0.0

            val location = Location("").apply {
                this.latitude = latitude
                this.longitude = longitude
            }

            Log.d("latitude > $latitude, longitude > $longitude")

            addressList?.let { addressList ->
                addressList.filter { it.isEnable == true }
                    .forEach { address ->
                        val destination = Location("").apply {
                            this.latitude = address.latitude ?: 0.0
                            this.longitude = address.longitude ?: 0.0
                        }
                        if (location.distanceTo(destination) <= 50) {
                            SendNotification(applicationContext, notificationId)
                        }
                    }
            }
        }
    }

    override suspend fun doWork(): Result {
        addressList = inputData.getStringArray(ADDRESS_LIST)?.map { it.decode<Address>(Json) }?.toTypedArray() ?: return Result.failure()
        createLocationCallback()
        setForeground(createForegroundInfo())
        return Result.success()
    }

    private fun hasPermission(): Boolean =
        ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

    @SuppressLint("MissingPermission")
    fun createLocationCallback() {
        if (hasPermission()) {
            LocationServices.getFusedLocationProviderClient(applicationContext).requestLocationUpdates(
                LocationRequest.create().apply {
                    interval = 4000
                    fastestInterval = 2000
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                },
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    // Creates an instance of ForegroundInfo which can be used to update the
    // ongoing notification.
    private fun createForegroundInfo(): ForegroundInfo {
        val id = applicationContext.getString(R.string.notification_channel_id)
        val title = applicationContext.getString(R.string.notification_channel_description)
        val cancel = applicationContext.getString(R.string.notification_channel_name)
        val progress = "GPS 정보를 수집중입니다."
        // This PendingIntent can be used to cancel the worker
        val intent = WorkManager.getInstance(applicationContext)
            .createCancelPendingIntent(getId())

        // Create a Notification channel if necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }

        val notification = NotificationCompat.Builder(applicationContext, id)
            .setContentTitle(title)
            .setTicker(title)
            .setContentText(progress)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            // Add the cancel action to the notification which can
            // be used to cancel the worker
            .addAction(android.R.drawable.ic_delete, cancel, intent)
            .build()

        return ForegroundInfo(notificationId, notification, FOREGROUND_SERVICE_TYPE_LOCATION)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        // Create a Notification channel
    }

    companion object {
        const val ADDRESS_LIST = "addressList"
    }
}