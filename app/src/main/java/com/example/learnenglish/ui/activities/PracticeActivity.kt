package com.example.learnenglish.ui.activities

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.learnenglish.R
import com.example.learnenglish.databinding.ActivityPracticeBinding
import com.example.learnenglish.extensions.setHtmlTitle
import com.example.learnenglish.ui.adapters.OptionAdapter

class PracticeActivity : AppCompatActivity() {
    private lateinit var optionAdapter: OptionAdapter
    private lateinit var binding: ActivityPracticeBinding

    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPracticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val receivedData = intent.getStringExtra(VocabularyActivity.Title)
        val title = receivedData ?: "Practice"

        supportActionBar?.setHtmlTitle(title, getColor(R.color.text))

        val listOptions: List<String> = listOf("10 Questions", "20 Questions", "30 Questions")

        optionAdapter = OptionAdapter(listOptions)
        binding.recyclerViewPractice.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewPractice.adapter = optionAdapter
    }

    companion object {
        const val Title = "Title"
    }
}