package com.example.learnenglish.ui.activities

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.learnenglish.R
import com.example.learnenglish.databinding.ActivitySettingsBinding
import com.example.learnenglish.extensions.setHtmlTitle
import com.example.learnenglish.receivers.MyBroadcastReceiver
import java.util.Calendar

class SettingsActivity : AppCompatActivity() {
    private var hour: Int = 0
    private var minute: Int = 0
    private lateinit var binding: ActivitySettingsBinding

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setHtmlTitle(getString(R.string.settings))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding.numberPickerHour.minValue = 0
        binding.numberPickerHour.maxValue = 23
        binding.numberPickerHour.value = 21
        hour = 21

        binding.numberPickerMinute.minValue = 0
        binding.numberPickerMinute.maxValue = 59
        binding.numberPickerMinute.value = 0

        binding.numberPickerHour.setOnValueChangedListener { _, _, newHour ->
            setHour(newHour)
        }
        binding.numberPickerMinute.setOnValueChangedListener { _, _, newMinute ->
            setMinute(newMinute)
        }
        binding.buttonSet.setOnClickListener {
            val intent = Intent(this, MyBroadcastReceiver::class.java)
            intent.putExtra("hour", hour)
            intent.putExtra("minute", minute)
            sendBroadcast(intent)
//            handleTime(hour, minute, this)
        }
    }

    private fun setHour(newHour: Int) {
        hour = newHour
    }

    private fun setMinute(newMinute: Int) {
        minute = newMinute
    }

    private fun handleTime(hour: Int, minute: Int, context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val notificationIntent = Intent("com.example.learnenglish.ALARM_ACTION")
        val pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)

        val notificationTime = calendar.timeInMillis
        val now = System.currentTimeMillis()

        if (notificationTime <= now) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        val timeUntilNotification = calendar.timeInMillis - System.currentTimeMillis()

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeUntilNotification, AlarmManager.INTERVAL_DAY, pendingIntent)
    }
}