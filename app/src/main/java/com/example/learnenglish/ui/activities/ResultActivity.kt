package com.example.learnenglish.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.learnenglish.R
import com.example.learnenglish.databinding.ActivityResultBinding
import com.example.learnenglish.extensions.setHtmlTitle

class ResultActivity : AppCompatActivity() {
    private var titleScreen: String? = ""
    private var result: String? = ""
    private var questions: String? = ""
    private var listQuestions: String? = ""
    private var listAnswers: String? = ""
    private var isListeningActivity: String = "false"
    private lateinit var binding: ActivityResultBinding

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        titleScreen = intent.getStringExtra(Title) ?: getString(R.string.title_result)
        result = intent.getStringExtra(Result) ?: ""
        questions = intent.getStringExtra(QuantityQuestion) ?: ""
        listQuestions = intent.getStringArrayExtra(ListQuestions)?.joinToString(", ") ?: ""
        listAnswers = intent.getStringArrayExtra(ListAnswers)?.joinToString(". ") ?: ""
        isListeningActivity = intent.getStringExtra(isListening).toString()

        supportActionBar?.setHtmlTitle(titleScreen!!)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        binding.textViewResult.text = "${result ?: "0"}/${questions ?: "0"}"

        if (questions?.toInt() == 10) {
            binding.textViewNotification.text = result?.toDouble()?.let { giveFeedback(it) }

            result?.let { giveEmotion(it.toDouble()) }
                ?.let { binding.imageViewEmotion.setImageResource(it) }
        } else if (questions?.toInt() == 20) {
            binding.textViewNotification.text = result?.toDouble()?.let { giveFeedback(it / 2) }

            result?.let { giveEmotion(it.toDouble()) }
                ?.let { binding.imageViewEmotion.setImageResource(it / 2) }
        } else if (questions?.toInt() == 30) {
            binding.textViewNotification.text = result?.toDouble()?.let { giveFeedback(it / 3) }

            result?.let { giveEmotion(it.toDouble()) }
                ?.let { binding.imageViewEmotion.setImageResource(it / 3) }
        }

        binding.buttonBack.text =
            if (isListeningActivity == "true") getString(R.string.text_button_back_listen) else binding.buttonBack.text

        binding.buttonBack.setOnClickListener {
            handleNavigateBack(isListeningActivity)
        }

        binding.buttonDetailResult.setOnClickListener {
            intent = Intent(this, ViewDetailResultActivity::class.java).apply {
                putExtra(ViewDetailResultActivity.Title, getString(R.string.title_detail_result))
                putExtra(ViewDetailResultActivity.ListQuestions, listQuestions)
                putExtra(ViewDetailResultActivity.ListAnswers, listAnswers)
                putExtra(ViewDetailResultActivity.isListening, isListeningActivity)
            }

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
        val intent = if (isListening == "true") {
            Intent(this, ListeningActivity::class.java).apply {
                putExtra(ListeningActivity.Title, getString(R.string.title_listening))
            }
        } else {
            Intent(this, PracticeActivity::class.java).apply {
                intent.putExtra(PracticeActivity.Title, getString(R.string.title_practice))
            }
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