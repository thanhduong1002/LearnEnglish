package com.example.learnenglish.ui.activities

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import com.example.learnenglish.R
import com.example.learnenglish.databinding.ActivitySettingsBinding
import com.example.learnenglish.extensions.setHtmlTitle
import com.example.learnenglish.receivers.MyBroadcastReceiver
import java.util.Calendar

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private var hour: Int = 0
    private var minute: Int = 0

    @SuppressLint("MissingPermission", "ResourceType")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setHtmlTitle(getString(R.string.settings), getColor(R.color.text))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val sharedPreferences = getSharedPreferences("Mode", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val nightMode = sharedPreferences.getBoolean("night", false)

        if (nightMode) {
            binding.materialSwitch.isChecked = true
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val areNotificationsEnabled = notificationManager.areNotificationsEnabled()

        binding.materialSwitchNotification.isChecked = areNotificationsEnabled

        binding.materialSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor.putBoolean("night", false)
                editor.apply()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor.putBoolean("night", true)
                editor.apply()
            }
        }
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
        binding.buttonApply.setOnClickListener {
            handleApply()
        }
    }

    private fun handleApply() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, MyBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

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
        val timeDelay = SystemClock.elapsedRealtime() + timeUntilNotification
        Log.d("time", "onCreate: $timeUntilNotification")
        alarmManager.set(AlarmManager.ELAPSED_REALTIME, timeDelay, pendingIntent)
    }

    private fun setHour(newHour: Int) {
        hour = newHour
    }

    private fun setMinute(newMinute: Int) {
        minute = newMinute
    }
}