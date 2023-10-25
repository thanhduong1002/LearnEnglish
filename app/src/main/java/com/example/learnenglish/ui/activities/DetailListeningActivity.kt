package com.example.learnenglish.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.learnenglish.R
import com.example.learnenglish.ui.fragments.ListenAndWriteFragment

class DetailListeningActivity : AppCompatActivity() {
    private var quantity: Int = 1
    private var receivedData: String? = ""
    private var result: Int = 0
    private var question: String = ""
    private var answer: String = ""
    private var isTrue: Boolean = false
    private var arrayQuestions: ArrayList<String> = ArrayList()
    private var arrayAnswers: ArrayList<String> = arrayListOf("example")

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_listening)

        receivedData = intent.getStringExtra(DetailPracticeActivity.Quantity)

        supportActionBar?.title =
            Html.fromHtml(
                "<font color=\"#442C2E\">$quantity/$receivedData</font>",
                Html.FROM_HTML_MODE_LEGACY
            )

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

    private fun addNewAnswer(newAnswer: String) {
        arrayAnswers.add(newAnswer)
    }

    fun replaceOrAddAnswer(newAnswer: String, number: Int) {
        if (number >= 0 && number < arrayAnswers.size) {
            arrayAnswers[number] = newAnswer
        } else {
            addNewAnswer(newAnswer)
        }
    }

    fun getListQuestions(): ArrayList<String> {
        return arrayQuestions
    }

    fun getQuantity(): Int {
        return quantity
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private fun checkAndUpdate(questions: Int) {
        val buttonCheck: Button = findViewById(R.id.buttonCheck)
        val forgetTextView: TextView = findViewById(R.id.textViewForget)

        forgetTextView.paintFlags = forgetTextView.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        updateFragment(quantity)

        buttonCheck.setOnClickListener {
            if (answer == "") replaceOrAddAnswer("", quantity)

            setNewResult(isTrue)

            quantity++

            supportActionBar?.title =
                Html.fromHtml(
                    "<font color=\"#442C2E\">$quantity/$receivedData</font>",
                    Html.FROM_HTML_MODE_LEGACY
                )

            if (quantity == questions + 1) {
                intent = Intent(this, ResultActivity::class.java)

                intent.putExtra(ResultActivity.Title, getString(R.string.title_result))
                intent.putExtra(ResultActivity.Result, result.toString())
                intent.putExtra(ResultActivity.QuantityQuestion, questions.toString())
                intent.putExtra(ResultActivity.ListQuestions, arrayQuestions.toTypedArray())
                intent.putExtra(ResultActivity.ListAnswers, arrayAnswers.toTypedArray())
                intent.putExtra(ResultActivity.isListening, "true")

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

                intent.putExtra(ResultActivity.QuantityQuestion, questions.toString())

                startActivity(intent)
            }

            updateFragment(quantity)
        }
    }

    private fun updateFragment(number: Int) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val fragment = if (number % 2 != 0) {
            ListenAndWriteFragment()
        } else {
            ListenAndWriteFragment()
        }

        fragmentTransaction.replace(R.id.mainContainer, fragment)
        fragmentTransaction.commit()
    }

    companion object {
        const val Quantity = "Quantity"
    }
}