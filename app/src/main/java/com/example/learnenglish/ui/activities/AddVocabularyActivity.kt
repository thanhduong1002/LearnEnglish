package com.example.learnenglish.ui.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Typeface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.text.HtmlCompat
import com.example.learnenglish.R
import com.example.learnenglish.data.dao.DetailVocabularyDao
import com.example.learnenglish.data.local.database.AppDatabase
import com.example.learnenglish.data.models.DetailVocabulary
import com.example.learnenglish.data.repositories.DetailVocabularyRepository
import com.example.learnenglish.ui.viewmodels.DetailVocabularyViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddVocabularyActivity : AppCompatActivity() {
    private lateinit var newVocabulary: DetailVocabulary
    private lateinit var database: AppDatabase
    private lateinit var detailVocabularyDao: DetailVocabularyDao
    private lateinit var detailVocabularyRepository: DetailVocabularyRepository
    private lateinit var detailVocabularyViewModel: DetailVocabularyViewModel

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vocabulary)

        supportActionBar?.title =
            Html.fromHtml(
                "<font color=\"#442C2E\">${getString(R.string.title_add_new_word)}</font>",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val topicData = arrayOf(
            "Marketing",
            "Office",
            "Contract",
            "Computer",
            "Warranties",
            "Business Planning",
            "Electronics",
            "Job Advertising and Recruiting",
            "Applying and Interviewing",
            "Hiring and Training",
            "Salaries and Benefits",
            "Promotions, Pensions and Awards",
            "Conferences",
            "Other"
        )

        newVocabulary = DetailVocabulary("", "", "", "", "", "")

        val spinnerEnglish: Spinner = findViewById(R.id.spinnerEnglish)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, topicData)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEnglish.adapter = adapter

        spinnerEnglish.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedTopic = parent?.getItemAtPosition(position).toString()

                newVocabulary.topic = selectedTopic
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                newVocabulary.topic = "Other"
            }
        }

        val editTextEnglish: EditText = findViewById(R.id.editTextEnglish)
        val editTextMeaning: EditText = findViewById(R.id.editTextMeaning)
        val editTextPronunciation: EditText = findViewById(R.id.editTextPronunciation)
        val editTextExample: EditText = findViewById(R.id.editTextExample)
        val editTextExampleTranslation: EditText = findViewById(R.id.editTextExampleTranslation)
        val buttonAddVocabulary: Button = findViewById(R.id.buttonAddVocabulary)
        val dialog = AlertDialog.Builder(this)
        val textView = TextView(this)

        with(textView) {
            text = "Our alert"
            textSize = 20.0F
            setTypeface(null, Typeface.BOLD)
            gravity = Gravity.CENTER
        }

        buttonAddVocabulary.setOnClickListener {
            if (editTextEnglish.text.toString().trim() != "" || editTextMeaning.text.toString()
                    .trim() != "" || editTextPronunciation.text.toString()
                    .trim() != "" || editTextExample.text.toString()
                    .trim() != "" || editTextExampleTranslation.text.toString().trim() != ""
            ) {
                newVocabulary.english = editTextEnglish.text.toString()
                newVocabulary.vietnamese = editTextMeaning.text.toString()
                newVocabulary.spelling = editTextPronunciation.text.toString()
                newVocabulary.example = editTextExample.text.toString()
                newVocabulary.exampleVN = editTextExampleTranslation.text.toString()

                database = AppDatabase.getDatabase(this)
                detailVocabularyDao = database.detailVocabularyDao()
                detailVocabularyRepository = DetailVocabularyRepository(detailVocabularyDao)
                detailVocabularyViewModel = DetailVocabularyViewModel(detailVocabularyRepository)

                try {
                    dialog.setMessage("Successfully added vocabulary to the '${newVocabulary.topic}' topic.")
                    dialog.setNeutralButton("Dismiss") { dialog, _ ->
                        dialog.dismiss()
                    }

                    dialog.show()

                    CoroutineScope(Dispatchers.IO).launch {
                        detailVocabularyViewModel.insertNewVocabulary(newVocabulary)
                    }
                } catch (e: Exception) {
                    Log.e("DatabaseError", e.message, e)
                }
            } else {
                dialog.setMessage("Please fill in all fields in English.")
                dialog.setNeutralButton("Dismiss") { dialog, _ ->
                    dialog.dismiss()
                }

                dialog.show()
            }
        }
    }
}