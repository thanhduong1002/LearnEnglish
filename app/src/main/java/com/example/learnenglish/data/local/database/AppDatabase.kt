package com.example.learnenglish.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.learnenglish.data.dao.DetailVocabularyDao
import com.example.learnenglish.data.models.DetailVocabulary

@Database(entities = [DetailVocabulary::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun detailVocabularyDao(): DetailVocabularyDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context) = INSTANCE ?: synchronized(this) {
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app_database"
            ).build().apply {
                INSTANCE = this
            }
        }
    }
}