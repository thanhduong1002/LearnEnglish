package com.example.learnenglish.ui.activities

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.learnenglish.R
import com.example.learnenglish.ui.adapters.ListeningOptionAdapter

class ListeningActivity : AppCompatActivity() {
    private lateinit var listeningOptionAdapter: ListeningOptionAdapter

    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listening)

        val receivedData = intent.getStringExtra(Title)
        val title = receivedData ?: "Listening"

        supportActionBar?.title = Html.fromHtml(
            "<font color=\"#442C2E\">$title</font>",
            Html.FROM_HTML_MODE_LEGACY
        )
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val listListeningOptions: List<String> = listOf("10 Questions", "20 Questions", "30 Questions")
        val recyclerViewListening: RecyclerView = findViewById(R.id.recyclerViewListening)

        listeningOptionAdapter = ListeningOptionAdapter(listListeningOptions)
        recyclerViewListening.layoutManager = LinearLayoutManager(this)
        recyclerViewListening.adapter = listeningOptionAdapter
    }

    companion object {
        const val Title = "Title"
    }
}