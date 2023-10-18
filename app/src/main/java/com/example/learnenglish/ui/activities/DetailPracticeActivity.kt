package com.example.learnenglish.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.learnenglish.R
import com.example.learnenglish.data.dao.DetailVocabularyDao
import com.example.learnenglish.data.local.database.AppDatabase
import com.example.learnenglish.data.repositories.DetailVocabularyRepository
import com.example.learnenglish.ui.fragments.MultipleChoiceFragment
import com.example.learnenglish.ui.fragments.WordFillingFragment
import com.example.learnenglish.ui.viewmodels.DetailVocabularyViewModel

class DetailPracticeActivity : AppCompatActivity() {
    private var quantity: Int = 1
    private var receivedData: String? = ""
    private var result: Int = 0
    private var question: String = ""
    private var answer: String = ""
    private var isTrue: Boolean = false
    private var arrayQuestions: ArrayList<String> = ArrayList()
    private var arrayAnswers: ArrayList<String> = ArrayList()
    private lateinit var database: AppDatabase
    private lateinit var detailVocabularyDao: DetailVocabularyDao
    private lateinit var detailVocabularyRepository: DetailVocabularyRepository
    private lateinit var detailVocabularyViewModel: DetailVocabularyViewModel

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_practice)

        database = AppDatabase.getDatabase(this)
        detailVocabularyDao = database.detailVocabularyDao()
        detailVocabularyRepository = DetailVocabularyRepository(detailVocabularyDao)
        detailVocabularyViewModel = DetailVocabularyViewModel(detailVocabularyRepository)

        val receivedIntent = intent

        if (receivedIntent != null) {
            receivedData = receivedIntent.getStringExtra(Quantity)

            if (receivedData != null) {
                supportActionBar?.title =
                    Html.fromHtml(
                        "<font color=\"#442C2E\">$quantity/$receivedData</font>",
                        Html.FROM_HTML_MODE_LEGACY
                    )
            }
        }

        receivedData?.let { checkAndUpdate(it.toInt()) }
    }

    private fun compareAnswerAndQuestion(answer: String, question: String): Boolean {
        if (answer == "" || question == "") return false

        val resultCompare = answer.compareTo(question, ignoreCase = true)

        return resultCompare == 0
    }

    private fun setNewResult(isTrue: Boolean) {
        if (isTrue) {
            result++
        }
    }

    fun setQuestion(newQuestion: String) {
        question = newQuestion
    }

    fun setAnswer(newAnswer: String) {
        answer = newAnswer
        isTrue = compareAnswerAndQuestion(answer, question)
    }

    fun addNewQuestion(newQuestion: String) {
        arrayQuestions.add(newQuestion)
    }

    fun addNewAnswer(newAnswer: String) {
        arrayAnswers.add(newAnswer)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private fun checkAndUpdate(questions: Int) {
        val buttonCheck: Button = findViewById(R.id.buttonCheck)
        val forgetTextView: TextView = findViewById(R.id.textViewForget)

        forgetTextView.paintFlags = forgetTextView.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        updateFragment(quantity)

        buttonCheck.setOnClickListener {
            if (answer == "") addNewAnswer("")

            setNewResult(isTrue)

            quantity++

            supportActionBar?.title =
                Html.fromHtml(
                    "<font color=\"#442C2E\">$quantity/$receivedData</font>",
                    Html.FROM_HTML_MODE_LEGACY
                )

            if (quantity == questions + 1) {
                intent = Intent(this, ResultActivity::class.java)

                intent.putExtra(ResultActivity.Title, "Result")
                intent.putExtra(ResultActivity.Result, result.toString())
                intent.putExtra(ResultActivity.QuantityQuestion, questions.toString())
                intent.putExtra(ResultActivity.ListQuestions, arrayQuestions.toTypedArray())
                intent.putExtra(ResultActivity.ListAnswers, arrayAnswers.toTypedArray())

                startActivity(intent)
            }

            updateFragment(quantity)
        }

        forgetTextView.setOnClickListener {
            addNewAnswer("")

            quantity++

            supportActionBar?.title =
                Html.fromHtml(
                    "<font color=\"#442C2E\">$quantity/$receivedData</font>",
                    Html.FROM_HTML_MODE_LEGACY
                )

            if (quantity + 1 == receivedData?.toInt()) {
                intent = Intent(this, ResultActivity::class.java)

                intent.putExtra(ResultActivity.Result, result.toString())
                intent.putExtra(ResultActivity.QuantityQuestion, questions.toString())

                startActivity(intent)
            }

            updateFragment(quantity)
        }
    }

    private fun updateFragment(number: Int) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val fragment = if (number % 2 != 0) {
            MultipleChoiceFragment()
        } else {
            WordFillingFragment()
        }

        fragmentTransaction.replace(R.id.mainContainer, fragment)
        fragmentTransaction.commit()
    }

    companion object {
        const val Quantity = "Quantity"
    }
}