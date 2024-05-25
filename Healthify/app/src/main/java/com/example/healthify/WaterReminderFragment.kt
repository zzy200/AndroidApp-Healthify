package com.example.healthify

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.healthify.notification.WaterReminderAlarmReceiver


class WaterReminderFragment : Fragment(), View.OnClickListener {

    private lateinit var b1: Button
    private lateinit var b2: Button
    private lateinit var b3: Button
    private lateinit var b4: Button
    private lateinit var b5: Button
    private lateinit var b6: Button
    private lateinit var b7: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_water_reminder, container, false)


        b1 = view.findViewById(R.id.h4Btn)
        b2 = view.findViewById(R.id.h2Btn)
        b3 = view.findViewById(R.id.h1Btn)
        b4 = view.findViewById(R.id.min30Btn)
        b5 = view.findViewById(R.id.min15Btn)
        b6 = view.findViewById(R.id.stopBtn)
        b7 = view.findViewById(R.id.startBtn)

        b1.setOnClickListener(this)
        b2.setOnClickListener(this)
        b3.setOnClickListener(this)
        b4.setOnClickListener(this)
        b5.setOnClickListener(this)
        b6.setOnClickListener(this)
        b7.setOnClickListener(this)
        return view
    }

    override fun onClick(view: View) {
        val sharedPreferences = requireActivity().getSharedPreferences("SettingsData", Activity.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        var delay = sharedPreferences.getInt("water_delay", 300)

        when (view.id) {
            R.id.min15Btn, R.id.min30Btn, R.id.h1Btn, R.id.h2Btn, R.id.h4Btn -> {
                val selectedDelay = when (view.id) {
                    R.id.min15Btn -> 1//15
                    R.id.min30Btn -> 30
                    R.id.h1Btn -> 60
                    R.id.h2Btn -> 120
                    R.id.h4Btn -> 240
                    else -> 300 // Default value
                }
                editor.putInt("water_delay", selectedDelay)
                editor.apply()
                //delay = selectedDelay
                updateButtonColors(view.id)
            }
            R.id.startBtn -> {
                val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val alarmIntent = Intent(requireContext(), WaterReminderAlarmReceiver::class.java).apply { action = "com.example.healthify.WATER_REMINDER" }
                val pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), delay.toLong() * 60 * 1000, pendingIntent)//*60 * 1000
                editor.putString("water_reminder", "true")
                editor.apply()
                Toast.makeText(requireContext(), "Water Reminders Started", Toast.LENGTH_SHORT).show()
            }
            R.id.stopBtn -> {
                editor.putString("water_reminder", "false")
                editor.apply()
                val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val alarmIntent = Intent(requireContext(), WaterReminderAlarmReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
                alarmManager.cancel(pendingIntent)
                Toast.makeText(requireContext(), "Water Reminders Stopped", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateButtonColors(selectedButtonId: Int) {
        val buttons = arrayOf(b1, b2, b3, b4, b5)
        buttons.forEach { button ->
            if (button?.id == selectedButtonId) {
                button.setTextColor(Color.parseColor("#FFCC00"))
            } else {
                button?.setTextColor(Color.WHITE)
            }
        }
    }
}