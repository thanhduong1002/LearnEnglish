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

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        val textViewResult: TextView = findViewById(R.id.textViewResult)
        val buttonDetailResult: Button = findViewById(R.id.buttonDetailResult)
        val buttonBack: Button = findViewById(R.id.buttonBack)
        val textViewNotification: TextView = findViewById(R.id.textViewNotification)
        val imageViewEmotion: ImageView = findViewById(R.id.imageViewEmotion)
        val receivedIntent = intent

        if (receivedIntent != null) {
            receivedData = receivedIntent.getStringExtra(Title)
            result = receivedIntent.getStringExtra(Result)
            questions = receivedIntent.getStringExtra(QuantityQuestion)
            listQuestions = receivedIntent.getStringExtra(ListQuestions)

            if (receivedData != null) {
                supportActionBar?.title =
                    Html.fromHtml(
                        "<font color=\"#442C2E\">$receivedData</font>",
                        Html.FROM_HTML_MODE_LEGACY
                    )
            }

            if (result != null && questions != null) {
                textViewResult.text = "$result/$questions"
            }
        }

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

        buttonBack.setOnClickListener {
            intent = Intent(this, PracticeActivity::class.java)

            intent.putExtra(PracticeActivity.Title, "Practice")

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

    companion object {
        const val Result = "Result"
        const val Title = "Title"
        const val QuantityQuestion = "QuantityQuestion"
        const val ListQuestions = "ListQuestions"
    }
}