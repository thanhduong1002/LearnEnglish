package com.example.learnenglish.ui.activities

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.learnenglish.R
import com.example.learnenglish.data.dao.DetailVocabularyDao
import com.example.learnenglish.data.local.database.AppDatabase
import com.example.learnenglish.data.models.DetailResult
import com.example.learnenglish.data.repositories.DetailVocabularyRepository
import com.example.learnenglish.databinding.ActivityViewDetailResultBinding
import com.example.learnenglish.extensions.setHtmlTitle
import com.example.learnenglish.ui.adapters.DetailResultAdapter
import com.example.learnenglish.ui.viewmodels.DetailVocabularyViewModel

class ViewDetailResultActivity : AppCompatActivity() {
    private var titleScreen: String? = ""
    private var listQuestions: String? = ""
    private var listAnswers: String? = ""
    private var modifiedList: List<String> = listOf()
    private lateinit var detailResultAdapter: DetailResultAdapter
    private lateinit var database: AppDatabase
    private lateinit var detailVocabularyDao: DetailVocabularyDao
    private lateinit var detailVocabularyRepository: DetailVocabularyRepository
    private lateinit var detailVocabularyViewModel: DetailVocabularyViewModel
    private lateinit var binding: ActivityViewDetailResultBinding

    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityViewDetailResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val customIcon = ContextCompat.getDrawable(this, R.drawable.practice_icon)
        customIcon?.setTint(ContextCompat.getColor(this, R.color.text))

        database = AppDatabase.getDatabase(this)
        detailVocabularyDao = database.detailVocabularyDao()
        detailVocabularyRepository = DetailVocabularyRepository(detailVocabularyDao)
        detailVocabularyViewModel = DetailVocabularyViewModel(detailVocabularyRepository)

        titleScreen = intent.getStringExtra(Title) ?: ""
        listQuestions = intent.getStringExtra(ListQuestions) ?: ""
        listAnswers = intent.getStringExtra(ListAnswers) ?: ""

        supportActionBar?.setHtmlTitle(titleScreen!!, getColor(R.color.text))
        supportActionBar?.setHomeAsUpIndicator(customIcon)

        val listQuestion = listQuestions?.let { changeStringToList(it) }
        val listAnswer = listAnswers?.let { changeStringToOtherList(it) }
        val resultList = mutableListOf<DetailResult>()

        if (!listAnswer.isNullOrEmpty()) {
            modifiedList = listAnswer.drop(1)
        }

        if (listQuestion != null) {
            for (i in 0 until minOf(listQuestion.size, modifiedList.size)) {
                val question = listQuestion[i]
                val answer = modifiedList[i]
                val result = DetailResult(answer, question)

                resultList.add(result)
            }
        }

        detailResultAdapter = DetailResultAdapter(resultList, detailVocabularyViewModel)
        binding.recyclerViewDetailResult.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewDetailResult.adapter = detailResultAdapter

        detailResultAdapter.setIsListening(intent.getStringExtra(isListening).toString())
    }

    private fun changeStringToList(input: String) = input.split(", ").map { it.trim() }

    private fun changeStringToOtherList(input: String) = input.split(". ").map { it.trim() }

    companion object {
        const val Title = "Title"
        const val ListQuestions = "ListQuestions"
        const val ListAnswers = "ListAnswers"
        const val isListening = "isListening"
    }
}