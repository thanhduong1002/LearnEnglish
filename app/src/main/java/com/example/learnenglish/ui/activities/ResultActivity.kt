package com.example.learnenglish.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.learnenglish.R

class ResultActivity : AppCompatActivity() {
    private var receivedData: String? = ""
    private var result: String? = ""
    private var questions: String? = ""
    private var listQuestions: String? = ""
    private var listAnswers: String? = ""
    private var isListeningActivity: String = "false"

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val textViewResult: TextView = findViewById(R.id.textViewResult)
        val buttonDetailResult: Button = findViewById(R.id.buttonDetailResult)
        val buttonBack: Button = findViewById(R.id.buttonBack)
        val textViewNotification: TextView = findViewById(R.id.textViewNotification)
        val imageViewEmotion: ImageView = findViewById(R.id.imageViewEmotion)

        receivedData = intent.getStringExtra(Title) ?: getString(R.string.title_result)
        result = intent.getStringExtra(Result)
        questions = intent.getStringExtra(QuantityQuestion)
        listQuestions = intent.getStringArrayExtra(ListQuestions)?.joinToString(", ")
        listAnswers = intent.getStringArrayExtra(ListAnswers)?.joinToString(". ")
        isListeningActivity = intent.getStringExtra(isListening).toString()

        supportActionBar?.title =
            Html.fromHtml(
                "<font color=\"#442C2E\">$receivedData</font>",
                Html.FROM_HTML_MODE_LEGACY
            )
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        textViewResult.text = "${result ?: "0"}/${questions ?: "0"}"

        if (questions?.toInt() == 10) {
            textViewNotification.text = result?.toDouble()?.let { giveFeedback(it) }

            result?.let { giveEmotion(it.toDouble()) }
                ?.let { imageViewEmotion.setImageResource(it) }
        } else if (questions?.toInt() == 20) {
            textViewNotification.text = result?.toDouble()?.let { giveFeedback(it / 2) }

            result?.let { giveEmotion(it.toDouble()) }
                ?.let { imageViewEmotion.setImageResource(it / 2) }
        } else if (questions?.toInt() == 30) {
            textViewNotification.text = result?.toDouble()?.let { giveFeedback(it / 3) }

            result?.let { giveEmotion(it.toDouble()) }
                ?.let { imageViewEmotion.setImageResource(it / 3) }
        }

        if (isListeningActivity == "true") {
            buttonBack.text = getString(R.string.text_button_back_listen)
        }

        buttonBack.setOnClickListener {
            handleNavigateBack(isListeningActivity)
        }

        buttonDetailResult.setOnClickListener {
            intent = Intent(this, ViewDetailResultActivity::class.java)

            intent.putExtra(ViewDetailResultActivity.Title, getString(R.string.title_detail_result))
            intent.putExtra(ViewDetailResultActivity.ListQuestions, listQuestions)
            intent.putExtra(ViewDetailResultActivity.ListAnswers, listAnswers)
            intent.putExtra(ViewDetailResultActivity.isListening, isListeningActivity)

            startActivity(intent)
        }
    }

    private fun giveFeedback(score: Double): String {
        return when (score) {
            10.0 -> "Great job! You've achieved an excellent score."
            in 8.0..9.0 -> "Fantastic! Your performance is outstanding."
            in 5.0..7.0 -> "Keep it up! You're doing fine."
            in 0.0..4.0 -> "Don't worry, keep trying! You'll improve with practice."
            else -> "Something wrong"
        }
    }

    private fun giveEmotion(score: Double): Int {
        return when (score) {
            10.0 -> R.drawable.perfect
            in 8.0..9.0 -> R.drawable.satisfaction
            in 5.0..7.0 -> R.drawable.normal
            in 0.0..4.0 -> R.drawable.sad
            else -> R.drawable.invaluable
        }
    }

    private fun handleNavigateBack(isListening: String) {
        if (isListening == "true") {
            intent = Intent(this, ListeningActivity::class.java)

            intent.putExtra(ListeningActivity.Title, getString(R.string.title_listening))
        } else {
            intent = Intent(this, PracticeActivity::class.java)

            intent.putExtra(PracticeActivity.Title, getString(R.string.title_practice))
        }

        startActivity(intent)
    }

    companion object {
        const val Result = "Result"
        const val Title = "Title"
        const val QuantityQuestion = "QuantityQuestion"
        const val ListQuestions = "ListQuestions"
        const val ListAnswers = "ListAnswers"
        const val isListening = "isListening"
    }
}