package com.example.learnenglish.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.learnenglish.R
import com.example.learnenglish.databinding.ActivityDetailListeningBinding
import com.example.learnenglish.extensions.setHtmlTitle
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
    private lateinit var binding: ActivityDetailListeningBinding

    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailListeningBinding.inflate(layoutInflater)
        setContentView(binding.root)

        receivedData = intent.getStringExtra(DetailPracticeActivity.Quantity)

        supportActionBar?.setHtmlTitle("$quantity/$receivedData", getColor(R.color.text))

        receivedData?.let { checkAndUpdate(it.toInt()) }
    }

    private fun compareAnswerAndQuestion(answer: String, question: String): Boolean {
        if (answer == "" || question == "") return false

        val resultCompare = answer.compareTo(question, ignoreCase = true)

        return resultCompare == 0
    }

    private fun setNewResult(isTrue: Boolean) {
        result += if (isTrue) 1 else 0
    }

    fun setQuestion(newQuestion: String) {
        question = newQuestion
    }

    fun setAnswer(newAnswer: String) {
        answer = newAnswer
        isTrue = compareAnswerAndQuestion(answer, question)
    }

    fun addNewQuestion(newQuestion: String) = arrayQuestions.add(newQuestion)

    private fun addNewAnswer(newAnswer: String) = arrayAnswers.add(newAnswer)

    fun replaceOrAddAnswer(newAnswer: String, number: Int) {
        if (number >= 0 && number < arrayAnswers.size) {
            arrayAnswers[number] = newAnswer
        } else {
            addNewAnswer(newAnswer)
        }
    }

    fun getListQuestions() = arrayQuestions

    fun getQuantity() = quantity

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n", "ResourceType")
    private fun checkAndUpdate(questions: Int) {
        binding.textViewForget.paintFlags = binding.textViewForget.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        updateFragment(quantity)

        intent = Intent(this, ResultActivity::class.java)

        binding.buttonCheck.setOnClickListener {
            if (answer == "") replaceOrAddAnswer("", quantity)

            setNewResult(isTrue)

            quantity++

            supportActionBar?.setHtmlTitle("$quantity/$receivedData", getColor(R.color.text))

            if (quantity == questions + 1) {
                intent.apply {
                    putExtra(ResultActivity.Title, getString(R.string.title_result))
                    putExtra(ResultActivity.Result, result.toString())
                    putExtra(ResultActivity.QuantityQuestion, questions.toString())
                    putExtra(ResultActivity.ListQuestions, arrayQuestions.toTypedArray())
                    putExtra(ResultActivity.ListAnswers, arrayAnswers.toTypedArray())
                    putExtra(ResultActivity.isListening, "true")
                }.run {
                    startActivity(this)
                }
            }

            updateFragment(quantity)
        }

        binding.textViewForget.setOnClickListener {
            addNewAnswer("")

            quantity++

            supportActionBar?.setHtmlTitle("$quantity/$receivedData", getColor(R.color.text))

            if (quantity + 1 == receivedData?.toInt()) {
                intent.apply {
                    putExtra(ResultActivity.QuantityQuestion, questions.toString())
                }. run {
                    startActivity(this)
                }
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