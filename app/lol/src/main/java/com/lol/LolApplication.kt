package com.lol

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.os.Build
import android.provider.Settings
import com.bowoon.lol.R
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.HiltAndroidApp
import com.data.util.Log

@HiltAndroidApp
class LolApplication : Application()/*, Configuration.Provider*/ {
//    @Inject private lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()

        createFirebaseMessageToken()
        createNotificationChannel()
    }

//    override fun getWorkManagerConfiguration() =
//        Configuration.Builder()
//            .setWorkerFactory(workerFactory)
//            .build()

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
                // TODO 서버로 토큰값 저장
                val data = mapOf(
                    "id" to Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID),
                    "model" to Build.MODEL,
                    "brand" to Build.BRAND,
                    "token" to token
                )
                Log.d("Firebase FCM Token >>>>> $token")
                Log.d(data.toString())
            }
            .addOnFailureListener { e ->
                Log.e("Fetching FCM registration token failed", e)
            }
    }
}