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
import com.example.learnenglish.data.models.DetailResult
import com.example.learnenglish.data.repositories.DetailVocabularyRepository
import com.example.learnenglish.ui.adapters.DetailResultAdapter
import com.example.learnenglish.ui.viewmodels.DetailVocabularyViewModel

class ViewDetailResultActivity : AppCompatActivity() {
    private var receivedData: String? = ""
    private var listQuestions: String? = ""
    private var listAnswers: String? = ""
    private lateinit var detailResultAdapter: DetailResultAdapter
    private lateinit var database: AppDatabase
    private lateinit var detailVocabularyDao: DetailVocabularyDao
    private lateinit var detailVocabularyRepository: DetailVocabularyRepository
    private lateinit var detailVocabularyViewModel: DetailVocabularyViewModel

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_detail_result)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        database = AppDatabase.getDatabase(this)
        detailVocabularyDao = database.detailVocabularyDao()
        detailVocabularyRepository = DetailVocabularyRepository(detailVocabularyDao)
        detailVocabularyViewModel = DetailVocabularyViewModel(detailVocabularyRepository)

        val receivedIntent = intent

        if (receivedIntent != null) {
            receivedData = receivedIntent.getStringExtra(Title)
            listQuestions = receivedIntent.getStringExtra(ListQuestions)
            listAnswers = receivedIntent.getStringExtra(ListAnswers)

            if (receivedData != null) {
                supportActionBar?.title =
                    Html.fromHtml(
                        "<font color=\"#442C2E\">$receivedData</font>",
                        Html.FROM_HTML_MODE_LEGACY
                    )
            }
        }

        val recyclerViewDetailResult: RecyclerView = findViewById(R.id.recyclerViewDetailResult)

        val listQuestion = listQuestions?.let { changeStringToList(it) }
        val listAnswer = listAnswers?.let { changeStringToOtherList(it) }
        val resultList = mutableListOf<DetailResult>()

        if (listQuestion != null) {
            if (listAnswer != null) {
                for (i in 0 until minOf(listQuestion.size, listAnswer.size)) {
                    val question = listQuestion[i]
                    val answer = listAnswer[i]

                    val result = DetailResult(answer, question)
                    resultList.add(result)
                }
            }
        }

        detailResultAdapter = DetailResultAdapter(resultList, detailVocabularyViewModel)
        recyclerViewDetailResult.layoutManager = LinearLayoutManager(this)
        recyclerViewDetailResult.adapter = detailResultAdapter
    }

    private fun changeStringToList(input: String): List<String> {
        return input.split(", ").map { it.trim() }
    }

    private fun changeStringToOtherList(input: String): List<String> {
        return input.split(". ").map { it.trim() }
    }

    companion object {
        const val Title = "Title"
        const val ListQuestions = "ListQuestions"
        const val ListAnswers = "ListAnswers"
    }
}