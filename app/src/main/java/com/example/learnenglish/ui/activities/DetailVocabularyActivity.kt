package com.example.learnenglish.ui.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.learnenglish.R
import com.example.learnenglish.data.dao.DetailVocabularyDao
import com.example.learnenglish.data.local.database.AppDatabase
import com.example.learnenglish.data.models.DetailVocabulary
import com.example.learnenglish.data.repositories.DetailVocabularyRepository
import com.example.learnenglish.ui.adapters.DetailVocabularyAdapter
import com.example.learnenglish.ui.viewmodels.DetailVocabularyViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailVocabularyActivity : AppCompatActivity() {
    private lateinit var database: AppDatabase
    private lateinit var receivedData: String
    private lateinit var detailVocabularyDao: DetailVocabularyDao
    private lateinit var detailVocabularyRepository: DetailVocabularyRepository
    private lateinit var detailVocabularyViewModel: DetailVocabularyViewModel
    private lateinit var listDetailVocabulary: List<DetailVocabulary>
    private lateinit var detailVocabularyAdapter: DetailVocabularyAdapter

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_vocabulary)

        receivedData = intent.getStringExtra(Title).toString()

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title =
            Html.fromHtml(
                "<font color=\"#442C2E\">$receivedData</font>",
                Html.FROM_HTML_MODE_LEGACY
            )

        database = AppDatabase.getDatabase(this)

        CoroutineScope(Dispatchers.IO).launch {
            detailVocabularyDao = database.detailVocabularyDao()

            detailVocabularyRepository = DetailVocabularyRepository(detailVocabularyDao)
            detailVocabularyViewModel = DetailVocabularyViewModel(detailVocabularyRepository)

            listDetailVocabulary = detailVocabularyViewModel.getByTopic(receivedData)

            val textViewEmpty: TextView = findViewById(R.id.textViewEmpty)
            val recyclerViewDetailVocabulary: RecyclerView =
                findViewById(R.id.recyclerViewDetailVocabulary)

            if (listDetailVocabulary.isEmpty()) textViewEmpty.visibility = View.VISIBLE
            else textViewEmpty.visibility = View.INVISIBLE

            detailVocabularyAdapter =
                DetailVocabularyAdapter(listDetailVocabulary)
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