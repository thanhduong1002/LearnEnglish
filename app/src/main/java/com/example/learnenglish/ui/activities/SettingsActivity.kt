package com.example.learnenglish.ui.activities

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.util.Log
import android.widget.Button
import android.widget.NumberPicker
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.learnenglish.R
import com.example.learnenglish.receivers.MyBroadcastReceiver
import java.util.Calendar

class SettingsActivity : AppCompatActivity() {
    private var hour: Int = 0
    private var minute: Int = 0
    private val notificationId = 1
    private val handler = Handler()
    private val notificationDelayMillis = 10 * 60 * 1000

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportActionBar?.title = Html.fromHtml(
            "<font color=\"#442C2E\">${getString(R.string.settings)}</font>",
            Html.FROM_HTML_MODE_LEGACY
        )
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val numberPickerHour: NumberPicker = findViewById(R.id.numberPickerHour)
        val numberPickerMinute: NumberPicker = findViewById(R.id.numberPickerMinute)
        val buttonSet: Button = findViewById(R.id.buttonSet)

        numberPickerHour.minValue = 0
        numberPickerHour.maxValue = 23
        numberPickerHour.value = 21

        numberPickerMinute.minValue = 0
        numberPickerMinute.maxValue = 59
        numberPickerMinute.value = 0

        numberPickerHour.setOnValueChangedListener { _, _, newHour ->
            setHour(newHour)
        }
        numberPickerMinute.setOnValueChangedListener { _, _, newMinute ->
            setMinute(newMinute)
        }
        buttonSet.setOnClickListener {
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