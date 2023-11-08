package com.example.learnenglish.receivers

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.learnenglish.R
import com.example.learnenglish.data.dao.DetailVocabularyDao
import com.example.learnenglish.data.local.database.AppDatabase
import com.example.learnenglish.data.models.DetailVocabulary
import com.example.learnenglish.data.repositories.DetailVocabularyRepository
import com.example.learnenglish.ui.viewmodels.DetailVocabularyViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MyBroadcastReceiver : BroadcastReceiver() {
    private val notificationId = 1
    private lateinit var database: AppDatabase
    private lateinit var detailVocabularyDao: DetailVocabularyDao
    private lateinit var detailVocabularyRepository: DetailVocabularyRepository
    private lateinit var detailVocabularyViewModel: DetailVocabularyViewModel
    private lateinit var englishWord: DetailVocabulary

    @OptIn(DelicateCoroutinesApi::class)
    override fun onReceive(context: Context, intent: Intent) {
        showNotification(context)
    }

    private fun createNotificationChannel(context: Context, channelId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "My Notification Channel"
            val description = "Description for My Notification Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(channelId, name, importance).apply {
                this.description = description
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("MissingPermission")
    @DelicateCoroutinesApi
    private fun showNotification(context: Context) {
        val channelId = "MyNotificationChannel"
        createNotificationChannel(context, channelId)

        database = AppDatabase.getDatabase(context)
        detailVocabularyDao = database.detailVocabularyDao()
        detailVocabularyRepository = DetailVocabularyRepository(detailVocabularyDao)
        detailVocabularyViewModel = DetailVocabularyViewModel(detailVocabularyRepository)

        GlobalScope.launch(Dispatchers.IO) {
            englishWord = detailVocabularyViewModel.getRandomDetailVocabulary()

            val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setContentTitle("Vocabulary")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            val bigTextStyle = NotificationCompat.BigTextStyle()

            bigTextStyle.bigText(
                "${englishWord.english} - ${englishWord.spelling}\n" +
                        "\n"+
                        "Meaning: ${englishWord.vietnamese}\n" +
                        "\n"+
                        "Example: ${englishWord.example}\n" +
                        "\n"+
                        "Translation: ${englishWord.exampleVN}"
            )

            builder.setStyle(bigTextStyle)

            val notificationManager = NotificationManagerCompat.from(context)

            notificationManager.notify(notificationId, builder.build())
        }
    }
}