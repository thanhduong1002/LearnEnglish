package com.example.learnenglish.ui.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.learnenglish.R
import com.example.learnenglish.ui.adapters.OptionAdapter

class PracticeActivity : AppCompatActivity() {
    private lateinit var optionAdapter: OptionAdapter

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice)

        val receivedData = intent.getStringExtra(VocabularyActivity.Title)
        val title = receivedData ?: "Practice"

        supportActionBar?.title = Html.fromHtml(
            "<font color=\"#442C2E\">$title</font>",
            Html.FROM_HTML_MODE_LEGACY
        )

        val listOptions: List<String> = listOf("10 Questions", "20 Questions", "30 Questions")
        val recyclerViewPractices: RecyclerView = findViewById(R.id.recyclerViewPractice)

        optionAdapter = OptionAdapter(listOptions)
        recyclerViewPractices.layoutManager = LinearLayoutManager(this)
        recyclerViewPractices.adapter = optionAdapter
    }

    companion object {
        const val Title = "Title"
    }
}