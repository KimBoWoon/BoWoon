package com.gps_alarm.ui.util

import android.app.PendingIntent
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bowoon.android.gps_alarm.R
import com.gps_alarm.ui.activities.GpsAlarmActivity

@Composable
fun SendNotification(notificationId: Int?) {
    val context = LocalContext.current
    val CHANNEL_ID = context.getString(R.string.notification_channel_id)
    val message = "목적지를 확인하세요."

    // Create an explicit intent for an Activity in your app
    val intent = Intent(context, GpsAlarmActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    val notification = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_background)
        .setContentTitle("GPS 알람")
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        // Set the intent that will fire when the user taps the notification
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .build().apply {
            flags = NotificationCompat.FLAG_INSISTENT
        }

    with(NotificationManagerCompat.from(context)) {
        // notificationId is a unique int for each notification that you must define
        notify(notificationId ?: 0, notification)
    }
}