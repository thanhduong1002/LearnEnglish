package com.example.learnenglish.ui.activities

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.learnenglish.databinding.ActivityListeningBinding
import com.example.learnenglish.extensions.setHtmlTitle
import com.example.learnenglish.ui.adapters.ListeningOptionAdapter

class ListeningActivity : AppCompatActivity() {
    private lateinit var listeningOptionAdapter: ListeningOptionAdapter
    private lateinit var binding: ActivityListeningBinding

    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListeningBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val receivedData = intent.getStringExtra(Title)
        val title = receivedData ?: "Listening"

        supportActionBar?.setHtmlTitle(title)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val listListeningOptions: List<String> = listOf("10 Questions", "20 Questions", "30 Questions")

        listeningOptionAdapter = ListeningOptionAdapter(listListeningOptions)
        binding.recyclerViewListening.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewListening.adapter = listeningOptionAdapter
    }

    companion object {
        const val Title = "Title"
    }
}