package com.example.healthify.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.healthify.R

class WaterReminderAlarmReceiver : BroadcastReceiver() {
        var nc = WaterReminderNotificationController
        var id = 0
        override fun onReceive(context: Context, intent: Intent) {
            Log.d("WaterReminderAlarmReceiver", "onReceive called with action: ${intent.action}")

            WaterReminderNotificationController.notify(context, context.getString(R.string.notification_string), id)
            id++
        }
}
