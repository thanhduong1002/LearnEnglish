package com.example.learnenglish.ui.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.learnenglish.data.dao.DetailVocabularyDao
import com.example.learnenglish.data.local.database.AppDatabase
import com.example.learnenglish.data.models.DetailVocabulary
import com.example.learnenglish.data.repositories.DetailVocabularyRepository
import com.example.learnenglish.databinding.ActivityDetailVocabularyBinding
import com.example.learnenglish.extensions.setHtmlTitle
import com.example.learnenglish.ui.adapters.DetailVocabularyAdapter
import com.example.learnenglish.ui.viewmodels.DetailVocabularyViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailVocabularyActivity : AppCompatActivity() {
    private lateinit var database: AppDatabase
    private lateinit var titleTopic: String
    private lateinit var detailVocabularyDao: DetailVocabularyDao
    private lateinit var detailVocabularyRepository: DetailVocabularyRepository
    private lateinit var detailVocabularyViewModel: DetailVocabularyViewModel
    private lateinit var listDetailVocabulary: List<DetailVocabulary>
    private lateinit var detailVocabularyAdapter: DetailVocabularyAdapter
    private lateinit var binding: ActivityDetailVocabularyBinding

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailVocabularyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        titleTopic = intent.getStringExtra(Title).toString()

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHtmlTitle(titleTopic)

        database = AppDatabase.getDatabase(this)

        CoroutineScope(Dispatchers.IO).launch {
            detailVocabularyDao = database.detailVocabularyDao()
            detailVocabularyRepository = DetailVocabularyRepository(detailVocabularyDao)
            detailVocabularyViewModel = DetailVocabularyViewModel(detailVocabularyRepository)

            listDetailVocabulary = detailVocabularyViewModel.getByTopic(titleTopic)

            binding.textViewEmpty.visibility =
                if (listDetailVocabulary.isEmpty()) View.VISIBLE else View.INVISIBLE

            detailVocabularyAdapter =
                DetailVocabularyAdapter(listDetailVocabulary)
            binding.recyclerViewDetailVocabulary.layoutManager =
                LinearLayoutManager(this@DetailVocabularyActivity)

            runOnUiThread {
                binding.recyclerViewDetailVocabulary.adapter = detailVocabularyAdapter
            }
        }
    }

    companion object {
        const val Title = "Title"
    }
}