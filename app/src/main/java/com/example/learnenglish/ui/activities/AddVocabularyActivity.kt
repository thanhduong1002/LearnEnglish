package com.example.learnenglish.ui.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Typeface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.learnenglish.R
import com.example.learnenglish.data.dao.DetailVocabularyDao
import com.example.learnenglish.data.local.database.AppDatabase
import com.example.learnenglish.data.models.DetailVocabulary
import com.example.learnenglish.data.repositories.DetailVocabularyRepository
import com.example.learnenglish.databinding.ActivityAddVocabularyBinding
import com.example.learnenglish.extensions.setHtmlTitle
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
    private lateinit var binding: ActivityAddVocabularyBinding

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddVocabularyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setHtmlTitle(getString(R.string.title_add_new_word))
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

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, topicData)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerEnglish.adapter = adapter

        binding.spinnerEnglish.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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

        val dialog = AlertDialog.Builder(this)
        val textView = TextView(this)

        with(textView) {
            text = "Our alert"
            textSize = 20.0F
            setTypeface(null, Typeface.BOLD)
            gravity = Gravity.CENTER
        }

        binding.buttonAddVocabulary.setOnClickListener {
            if (binding.editTextEnglish.text.toString().trim() != "" || binding.editTextMeaning.text.toString()
                    .trim() != "" || binding.editTextPronunciation.text.toString()
                    .trim() != "" || binding.editTextExample.text.toString()
                    .trim() != "" || binding.editTextExampleTranslation.text.toString().trim() != ""
            ) {
                newVocabulary.english = binding.editTextEnglish.text.toString()
                newVocabulary.vietnamese = binding.editTextMeaning.text.toString()
                newVocabulary.spelling = binding.editTextPronunciation.text.toString()
                newVocabulary.example = binding.editTextExample.text.toString()
                newVocabulary.exampleVN = binding.editTextExampleTranslation.text.toString()

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