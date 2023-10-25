package com.example.learnenglish.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.learnenglish.R
import com.example.learnenglish.data.dao.DetailVocabularyDao
import com.example.learnenglish.data.local.database.AppDatabase
import com.example.learnenglish.data.repositories.DetailVocabularyRepository
import com.example.learnenglish.ui.activities.DetailListeningActivity
import com.example.learnenglish.ui.viewmodels.DetailVocabularyViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import java.util.Random

class ListenAndWriteFragment : Fragment() {
    private lateinit var database: AppDatabase
    private lateinit var detailVocabularyDao: DetailVocabularyDao
    private lateinit var detailVocabularyRepository: DetailVocabularyRepository
    private lateinit var detailVocabularyViewModel: DetailVocabularyViewModel
    private lateinit var listEnglishes: List<String>
    private lateinit var textForSpeech: String
    private lateinit var textToSpeech: TextToSpeech
    private var isHint: Boolean = true
    private var quantity: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_listen_and_write, container, false)
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    @DelicateCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = AppDatabase.getDatabase(requireContext())
        detailVocabularyDao = database.detailVocabularyDao()
        detailVocabularyRepository = DetailVocabularyRepository(detailVocabularyDao)
        detailVocabularyViewModel = DetailVocabularyViewModel(detailVocabularyRepository)

        val editTextAnswer: EditText = view.findViewById(R.id.editAnswer)
        val imageSaved: ImageView = view.findViewById(R.id.imageSaved)
        val textViewSaved: TextView = view.findViewById(R.id.textViewSaved)
        val textViewSuggestion: TextView = view.findViewById(R.id.textViewSuggestion)
        val buttonSpeaker: Button = view.findViewById(R.id.speakerButton)
        val imageHint: ImageView = view.findViewById(R.id.imageHint)
        val textViewHint: TextView = view.findViewById(R.id.textViewHint)
        val detailListeningActivity = requireActivity() as DetailListeningActivity

        detailListeningActivity.setAnswer("")

        GlobalScope.launch(Dispatchers.IO) {
            listEnglishes = detailVocabularyViewModel.getAllEnglishesWords()

            val random = Random()
            val randomIndex = random.nextInt(listEnglishes.size)
            val english = listEnglishes[randomIndex]
            val vietnamese = detailVocabularyViewModel.getVietnameseByEnglish(english)

            textForSpeech = english

            detailListeningActivity.setQuestion(english)
            detailListeningActivity.addNewQuestion(english)

            withContext(Dispatchers.Main) {
                textViewSuggestion.text = "It has ${english.length} characters"
                textViewHint.text = vietnamese
                editTextAnswer.setOnFocusChangeListener { _ , hasFocus ->
                    if (hasFocus) {
                        textViewSaved.visibility = View.INVISIBLE
                    }
                }
            }
        }

        buttonSpeaker.setOnClickListener {
            handleSpeaker(textForSpeech)
        }

        imageHint.setOnClickListener {
            if (isHint) {
                imageHint.setColorFilter(ContextCompat.getColor(requireContext(), R.color.yellow))

                textViewHint.visibility = View.VISIBLE
            }
            else {
                imageHint.clearColorFilter()

                textViewHint.visibility = View.INVISIBLE
            }

            isHint = !isHint
        }

        imageSaved.setOnClickListener {
            textViewSaved.visibility = View.VISIBLE

            detailListeningActivity.setAnswer(editTextAnswer.text.toString())

            quantity = detailListeningActivity.getQuantity()

            detailListeningActivity.replaceOrAddAnswer(editTextAnswer.text.toString(), quantity)

            val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(editTextAnswer.windowToken, 0)

            editTextAnswer.clearFocus()
        }
    }

    private fun handleSpeaker(textRead: String) {
        textToSpeech = TextToSpeech(requireContext()) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val locale = Locale("en", "US")
                val result = textToSpeech.setLanguage(locale)

                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "No support this language")
                } else {
                    textToSpeech.speak(textRead, TextToSpeech.QUEUE_FLUSH, null, null)
                }
            } else {
                Log.e("TTS", "Failed")
            }
        }
    }
}