package com.example.healthify.notification

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import android.util.Log
import com.example.healthify.MainActivity
import com.example.healthify.R

object WaterReminderNotificationController {

    private const val NOTIFICATION_TAG = "Water Reminder"
    private const val CHANNEL_ID = "water_reminder_channel"

    @SuppressLint("StringFormatInvalid")
    fun notify(context: Context, notificationTicker: String, number: Int) {
        Log.d("WaterReminderNotificationController", "notify called with ticker: $notificationTicker, number: $number")

        val res: Resources = context.resources
        val picture = BitmapFactory.decodeResource(res, R.mipmap.ic_launcher)
        val title = "Water Reminder"
        val text = res.getString(R.string.notification_text, notificationTicker)

        val backIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, backIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        // 创建通知渠道
        createNotificationChannel(context)

        val notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setDefaults(Notification.DEFAULT_ALL)
            .setSmallIcon(R.drawable.ic_stat_s_controller)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setLargeIcon(picture)
            .setTicker(notificationTicker)
            .setNumber(number)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(text)
                    .setBigContentTitle(title)
                    .setSummaryText("Drink Some Water"))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setSound(notificationSound)

        notify(context, builder.build())
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Water Reminder Channel"
            val descriptionText = "Channel for Water Reminder notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun notify(context: Context, notification: Notification) {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(NOTIFICATION_TAG, 0, notification)
    }

}
