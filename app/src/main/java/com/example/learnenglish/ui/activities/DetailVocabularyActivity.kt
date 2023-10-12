package com.example.learnenglish.ui.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.learnenglish.R
import com.example.learnenglish.data.dao.DetailVocabularyDao
import com.example.learnenglish.data.local.database.AppDatabase
import com.example.learnenglish.data.models.DetailVocabulary
import com.example.learnenglish.data.repositories.DetailVocabularyRepository
import com.example.learnenglish.ui.adapters.DetailVocabularyAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailVocabularyActivity : AppCompatActivity() {
    private lateinit var database: AppDatabase
    private lateinit var receivedData: String
    private lateinit var detailVocabularyDao: DetailVocabularyDao
    private lateinit var detailVocabularyRepository: DetailVocabularyRepository
    private lateinit var listDetailVocabulary: List<DetailVocabulary>
    private lateinit var detailVocabularyAdapter: DetailVocabularyAdapter

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_vocabulary)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val receivedIntent = intent

        if (receivedIntent != null) {
            receivedData = receivedIntent.getStringExtra(Title).toString()

            supportActionBar?.title =
                Html.fromHtml("<font color=\"#442C2E\">$receivedData</font>", Html.FROM_HTML_MODE_LEGACY)
        }

        database = AppDatabase.getDatabase(this)

        CoroutineScope(Dispatchers.IO).launch {
            detailVocabularyDao = database.detailVocabularyDao()

            detailVocabularyRepository = DetailVocabularyRepository(detailVocabularyDao)
            listDetailVocabulary = detailVocabularyRepository.getByTopic(receivedData)

            val recyclerViewDetailVocabulary: RecyclerView =
                findViewById(R.id.recyclerViewDetailVocabulary)

            detailVocabularyAdapter =
                DetailVocabularyAdapter( listDetailVocabulary)
            recyclerViewDetailVocabulary.layoutManager =
                LinearLayoutManager(this@DetailVocabularyActivity)

            runOnUiThread {
                recyclerViewDetailVocabulary.adapter = detailVocabularyAdapter
            }
        }
    }

    companion object {
        const val Title = "Title"
    }
}