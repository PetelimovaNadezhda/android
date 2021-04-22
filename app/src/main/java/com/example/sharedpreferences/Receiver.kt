package com.example.sharedpreferences

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

class Receiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val intent = Intent(context, MainActivity::class.java)
        val pIntent = PendingIntent.getActivity(context, 0, intent, 0)
        //https://developer.android.com/training/notify-user/build-notification#builder
        val notificationBuilder = NotificationCompat.Builder(context, "channel1")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Reminder")
            .setContentText("Time to study!")
            .setStyle(NotificationCompat.BigTextStyle())
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pIntent)
        //https://developer.android.com/training/notify-user/build-notification#Priority
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "channel1"
            val descriptionText = "channel1"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("channel1", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            notificationManager.notify(1, notificationBuilder.build())
        }
    }
}