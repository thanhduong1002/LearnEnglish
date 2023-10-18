package com.example.learnenglish.ui.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.learnenglish.R
import com.example.learnenglish.data.models.Vocabulary
import com.example.learnenglish.ui.adapters.VocabularyAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class VocabularyActivity : AppCompatActivity() {
    private lateinit var vocabularyAdapter: VocabularyAdapter

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vocabulary)

        val receivedIntent = intent

        if (receivedIntent != null) {
            val receivedData = receivedIntent.getStringExtra(Title)

            if (receivedData != null) {
                supportActionBar?.title = Html.fromHtml("<font color=\"#442C2E\">$receivedData</font>", Html.FROM_HTML_MODE_LEGACY)
            } else supportActionBar?.title = Html.fromHtml("<font color=\"#442C2E\">Vocabulary</font>", Html.FROM_HTML_MODE_LEGACY)
        }

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
        )

        val recyclerViewVocabulary: RecyclerView = findViewById(R.id.recyclerViewVocabularies)

        vocabularyAdapter = VocabularyAdapter(listVocabularies)
        recyclerViewVocabulary.layoutManager = LinearLayoutManager(this)
        recyclerViewVocabulary.adapter = vocabularyAdapter

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