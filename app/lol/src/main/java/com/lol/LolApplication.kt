package com.lol

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.os.Build
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.HiltAndroidApp
import util.Log

@HiltAndroidApp
class LolApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        createFirebaseMessageToken()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            (getSystemService(NOTIFICATION_SERVICE) as? NotificationManager)?.apply {
                createNotificationChannelGroup(NotificationChannelGroup("FCM", getString(R.string.firebase_notification_channel_id)))
                createNotificationChannel(
                    NotificationChannel(
                        getString(R.string.firebase_notification_channel_id),
                        getString(R.string.firebase_notification_channel_id),
                        NotificationManager.IMPORTANCE_DEFAULT
                    ).apply {
                        description = "Firebase FCM"
                    }
                )
            }
        }
    }

    private fun createFirebaseMessageToken() {
        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { token ->
                Log.d("Firebase FCM Token >>>>> $token")
            }
            .addOnFailureListener { e ->
                Log.e("Fetching FCM registration token failed", e)
            }
    }
}