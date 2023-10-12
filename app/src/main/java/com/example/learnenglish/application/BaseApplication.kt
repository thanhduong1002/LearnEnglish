package com.example.learnenglish.application

import android.app.Application
import com.example.learnenglish.data.content.ContentDetailVocabulary
import com.example.learnenglish.data.local.database.AppDatabase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
class BaseApplication : Application() {
    private val database: AppDatabase by lazy {
        AppDatabase.getDatabase(this)
    }

    init {
        GlobalScope.launch(Dispatchers.IO) {
            val detailVocabularyDao = database.detailVocabularyDao()
            val detailVocabularies = ContentDetailVocabulary().loadDataDetailVocabulary()

            detailVocabularyDao.insertAll(detailVocabularies)
        }
    }
}