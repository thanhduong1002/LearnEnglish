package com.example.learnenglish.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.learnenglish.R
import com.example.learnenglish.data.models.Vocabulary
import com.example.learnenglish.databinding.ActivityVocabularyBinding
import com.example.learnenglish.extensions.setHtmlTitle
import com.example.learnenglish.ui.adapters.VocabularyAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class VocabularyActivity : AppCompatActivity() {
    private lateinit var vocabularyAdapter: VocabularyAdapter
    private lateinit var binding: ActivityVocabularyBinding

    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVocabularyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val receivedData = intent.getStringExtra(Title)
        val title = receivedData ?: "Vocabulary"

        supportActionBar?.setHtmlTitle(title, getColor(R.color.text))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val listVocabularies: List<Vocabulary> = listOf(
            Vocabulary("Marketing", R.drawable.marketing),
            Vocabulary("Office", R.drawable.office),
            Vocabulary("Contract", R.drawable.contract),
            Vocabulary("Computer", R.drawable.computer),
            Vocabulary("Warranties", R.drawable.warranties),
            Vocabulary("Business Planning", R.drawable.bussiness),
            Vocabulary("Electronics", R.drawable.electronics),
            Vocabulary("Job Advertising and Recruiting", R.drawable.jobadvertising),
            Vocabulary("Applying and Interviewing", R.drawable.interview),
            Vocabulary("Hiring and Training", R.drawable.hireandtrain),
            Vocabulary("Salaries and Benefits", R.drawable.salarybenefit),
            Vocabulary("Promotions, Pensions and Awards", R.drawable.awards),
            Vocabulary("Conferences", R.drawable.conference),
            Vocabulary("Other", R.drawable.other),
        )

        vocabularyAdapter = VocabularyAdapter(listVocabularies)
        binding.recyclerViewVocabularies.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewVocabularies.adapter = vocabularyAdapter

        val floatingActionButton: FloatingActionButton = findViewById(R.id.floatingActionButton)

        floatingActionButton.setOnClickListener {
            val intent = Intent(this, AddVocabularyActivity::class.java)

            startActivity(intent)
        }
    }

    companion object {
        const val Title = "Title"
    }
}