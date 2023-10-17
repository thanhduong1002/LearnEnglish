package com.example.learnenglish.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.learnenglish.R
import com.example.learnenglish.data.dao.DetailVocabularyDao
import com.example.learnenglish.data.local.database.AppDatabase
import com.example.learnenglish.data.repositories.DetailVocabularyRepository
import com.example.learnenglish.ui.activities.DetailPracticeActivity
import com.example.learnenglish.ui.viewmodels.DetailVocabularyViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Random

class WordFillingFragment : Fragment() {
    private lateinit var database: AppDatabase
    private lateinit var detailVocabularyDao: DetailVocabularyDao
    private lateinit var detailVocabularyRepository: DetailVocabularyRepository
    private lateinit var detailVocabularyViewModel: DetailVocabularyViewModel
    private lateinit var listVietnameses: List<String>
    private lateinit var englishWord: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_word_filling, container, false)
    }

    @DelicateCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = AppDatabase.getDatabase(requireContext())
        detailVocabularyDao = database.detailVocabularyDao()
        detailVocabularyRepository = DetailVocabularyRepository(detailVocabularyDao)
        detailVocabularyViewModel = DetailVocabularyViewModel(detailVocabularyRepository)

        val textViewQuestion: TextView = view.findViewById(R.id.textViewQuestion)
        val editTextAnswer: EditText = view.findViewById(R.id.editAnswer)
        val textViewSaved: TextView = view.findViewById(R.id.textViewSaved)
        val imageSaved: ImageView = view.findViewById(R.id.imageSaved)
        val constraintLayoutFilling: ConstraintLayout = view.findViewById(R.id.constraintLayoutFilling)
        val detailPracticeActivity = requireActivity() as DetailPracticeActivity

        detailPracticeActivity.setAnswer("")

        GlobalScope.launch(Dispatchers.IO) {
            listVietnameses = detailVocabularyViewModel.getAllVietnameseWords()

            val random = Random()
            val randomIndex = random.nextInt(listVietnameses.size)
            val vietnamese = listVietnameses[randomIndex]

            englishWord = detailVocabularyViewModel.getEnglishByVietnamese(vietnamese)

            detailPracticeActivity.setQuestion(englishWord)

            withContext(Dispatchers.Main) {
                textViewQuestion.text = vietnamese

                editTextAnswer.setOnFocusChangeListener { _ , hasFocus ->
                    if (hasFocus) {
                        editTextAnswer.hint = replaceWithUnderscores(englishWord)
                        textViewSaved.visibility = View.INVISIBLE
                    }
                }
            }
        }

        imageSaved.setOnClickListener {
            textViewSaved.visibility = View.VISIBLE

            detailPracticeActivity.setAnswer(editTextAnswer.text.toString())
        }

        constraintLayoutFilling.setOnClickListener {
            editTextAnswer.clearFocus()
        }
    }

    private fun replaceWithUnderscores(word: String): String {
        val charactersToReplace = setOf('a', 'e', 'i', 'o', 'u', 'c', 'n', "p")
        val result = StringBuilder()

        for (char in word) {
            if (char.lowercaseChar() in charactersToReplace) {
                result.append("_")
            } else {
                result.append(char)
            }
        }

        return result.toString()
    }
}