package com.example.learnenglish.ui.activities

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Dialog
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.Window.*
import android.widget.Button
import android.widget.NumberPicker
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.learnenglish.R
import com.example.learnenglish.databinding.ActivityMainBinding
import com.example.learnenglish.extensions.setHtmlTitle
import com.example.learnenglish.receivers.MyBroadcastReceiver
import com.example.learnenglish.ui.adapters.OptionAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class MainActivity : AppCompatActivity() {
    private lateinit var optionAdapter: OptionAdapter
    private lateinit var binding: ActivityMainBinding
    private var hour: Int = 0
    private var minute: Int = 0
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("ResourceType", "SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        val lastOpenTime = sharedPreferences.getString("lastOpenTime", "")
        val currentDate = SimpleDateFormat("yyyyMMdd").format(Date())

        if (lastOpenTime != currentDate) {
            showPopupDialog()
            sharedPreferences.edit().putString("lastOpenTime", currentDate).apply()
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setHtmlTitle(getString(R.string.app_name), getColor(R.color.text))

        val listOptions: List<String> = listOf("Vocabulary", "Practice", "Listening")

        optionAdapter = OptionAdapter(listOptions)
        binding.recyclerViewOptions.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewOptions.adapter = optionAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)

                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun showPopupDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_custom_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val numberPickerHour: NumberPicker = dialog.findViewById(R.id.numberPickerHour)
        val numberPickerMinute: NumberPicker = dialog.findViewById(R.id.numberPickerMinute)
        val setButton: Button = dialog.findViewById(R.id.buttonSet)
        val cancelButton: Button = dialog.findViewById(R.id.buttonCancel)

        numberPickerHour.minValue = 0
        numberPickerHour.maxValue = 23
        numberPickerHour.value = 21
        hour = 21

        numberPickerMinute.minValue = 0
        numberPickerMinute.maxValue = 59
        numberPickerMinute.value = 0

        numberPickerHour.setOnValueChangedListener { _, _, newHour ->
            setHour(newHour)
        }
        numberPickerMinute.setOnValueChangedListener { _, _, newMinute ->
            setMinute(newMinute)
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        setButton.setOnClickListener {
            handleSet()

            if (!checkEnabledNotifications()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    showEnableNotificationsDialog()
                }
            }

            dialog.dismiss()
        }

        dialog.show()
    }

    private fun setHour(newHour: Int) {
        hour = newHour
    }

    private fun setMinute(newMinute: Int) {
        minute = newMinute
    }

    private fun handleSet() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, MyBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showEnableNotificationsDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Enable Notifications")
            .setMessage("Please enable notifications for the best experience.")
            .setPositiveButton("Go to Settings") { _, _ ->
                sharedPreferences.edit().putBoolean("notificationsEnabled", true).apply()

                val intent = Intent().apply {
                    action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                    putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                }
                startActivity(intent)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun checkEnabledNotifications(): Boolean {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        return notificationManager.areNotificationsEnabled()
    }
}