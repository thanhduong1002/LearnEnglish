package com.example.learnenglish.ui.activities

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.learnenglish.R
import com.example.learnenglish.databinding.ActivityTranslationBinding
import com.example.learnenglish.extensions.setHtmlTitle
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions

class TranslationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTranslationBinding
    private var fromLanguageCode: Int = 0
    private var toLanguageCode: Int = 0

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTranslationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val receivedData = intent.getStringExtra(Title)
        val title = receivedData ?: "Translation"

        supportActionBar?.setHtmlTitle(title, getColor(R.color.text))

        val fromLanguage = arrayOf(
            "From",
            "English",
            "Afrikaans",
            "Arabic",
            "Belarusian",
            "Bulgarian",
            "Bengali",
            "Catalan",
            "Czech",
            "Welsh",
            "Hindi",
            "Urdu",
            "Vietnamese"
        )
        val toLanguage = arrayOf(
            "To",
            "English",
            "Afrikaans",
            "Arabic",
            "Belarusian",
            "Bulgarian",
            "Bengali",
            "Catalan",
            "Czech",
            "Welsh",
            "Hindi",
            "Urdu",
            "Vietnamese"
        )

        val adapterFrom = ArrayAdapter(this, android.R.layout.simple_spinner_item, fromLanguage)
        val adapterTo = ArrayAdapter(this, android.R.layout.simple_spinner_item, toLanguage)

        adapterFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapterTo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.idFromSpinner.adapter = adapterFrom
        binding.idToSpinner.adapter = adapterTo

        binding.idFromSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedLanguage = parent?.getItemAtPosition(position).toString()

                fromLanguageCode = getLanguageCode(selectedLanguage)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                fromLanguageCode = 0
            }
        }
        binding.idToSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedLanguage = parent?.getItemAtPosition(position).toString()

                toLanguageCode = getLanguageCode(selectedLanguage)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                fromLanguageCode = 0
            }
        }

        binding.translateButton.setOnClickListener {
            if (binding.editSource.text.toString().isEmpty()) {
                Toast.makeText(this, "Please enter your text to translate", Toast.LENGTH_SHORT)
                    .show()
            } else if (fromLanguageCode == 0) Toast.makeText(
                this,
                "Please select source language",
                Toast.LENGTH_SHORT
            ).show()
            else if (toLanguageCode == 0) Toast.makeText(
                this,
                "Please select the language to make translation",
                Toast.LENGTH_SHORT
            ).show()
            else translateText(fromLanguageCode, toLanguageCode, binding.editSource.text.toString())
        }

        binding.textViewResult.text = ""
    }

    companion object {
        const val Title = "Title"
    }

    private fun getLanguageCode(language: String): Int {
        return when (language) {
            "English" -> FirebaseTranslateLanguage.EN
            "Afrikaans" -> FirebaseTranslateLanguage.AF
            "Arabic" -> FirebaseTranslateLanguage.AR
            "Belarusian" -> FirebaseTranslateLanguage.BE
            "Bulgarian" -> FirebaseTranslateLanguage.BG
            "Bengali" -> FirebaseTranslateLanguage.BN
            "Catalan" -> FirebaseTranslateLanguage.CA
            "Czech" -> FirebaseTranslateLanguage.CS
            "Welsh" -> FirebaseTranslateLanguage.CY
            "Hindi" -> FirebaseTranslateLanguage.HI
            "Urdu" -> FirebaseTranslateLanguage.UR
            "Vietnamese" -> FirebaseTranslateLanguage.VI
            else -> 0
        }
    }

    @SuppressLint("SetTextI18n")
    private fun translateText(fromLanguageCode: Int, toLanguageCode: Int, sourceText: String) {
        binding.textViewResult.text = "Downloading Modal..."

        val options = FirebaseTranslatorOptions.Builder()
            .setSourceLanguage(fromLanguageCode)
            .setTargetLanguage(toLanguageCode)
            .build()
        val textTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options)

        textTranslator.downloadModelIfNeeded()
            .addOnSuccessListener {
                binding.textViewResult.text = "Translating..."
                textTranslator.translate(sourceText).addOnSuccessListener { translatedText ->
                    binding.textViewResult.text = translatedText
                }.addOnFailureListener { exception ->
                    binding.textViewResult.text = "Translation failed: ${exception.message}"
                }
            }
            .addOnFailureListener {
                binding.textViewResult.text = "Download model failed: ${it.message}"
            }
    }
}