package com.example.learnenglish.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.example.learnenglish.data.dao.DetailVocabularyDao
import com.example.learnenglish.data.local.database.AppDatabase
import com.example.learnenglish.data.repositories.DetailVocabularyRepository
import com.example.learnenglish.databinding.FragmentWordFillingBinding
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
    private lateinit var listEnglishes: List<String>
    private var quantity: Int = 0
    private lateinit var binding: FragmentWordFillingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWordFillingBinding.inflate(inflater, container, false)
        return binding.root
    }

    @DelicateCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = AppDatabase.getDatabase(requireContext())
        detailVocabularyDao = database.detailVocabularyDao()
        detailVocabularyRepository = DetailVocabularyRepository(detailVocabularyDao)
        detailVocabularyViewModel = DetailVocabularyViewModel(detailVocabularyRepository)

        val detailPracticeActivity = requireActivity() as DetailPracticeActivity

        detailPracticeActivity.setAnswer("")

        GlobalScope.launch(Dispatchers.IO) {
            listEnglishes = detailVocabularyViewModel.getAllEnglishesWords()

            val listEnglishToRemove: List<String> = detailPracticeActivity.getListQuestions().toList()
            val newListEnglishes = listEnglishes.filter { englishWord ->
                !listEnglishToRemove.any { englishWordToRemove ->
                    englishWord == englishWordToRemove
                }
            }
            val random = Random()
            val randomIndex = random.nextInt(newListEnglishes.size)
            val english = newListEnglishes[randomIndex]
            val vietnamese = detailVocabularyViewModel.getVietnameseByEnglish(english)

            detailPracticeActivity.setQuestion(english)
            detailPracticeActivity.addNewQuestion(english)

            withContext(Dispatchers.Main) {
                binding.textViewQuestion.text = vietnamese
                binding.editAnswer.setOnFocusChangeListener { _ , hasFocus ->
                    if (hasFocus) {
                        binding.editAnswer.hint = replaceWithUnderscores(english)
                        binding.textViewSaved.visibility = View.INVISIBLE
                    }
                }
            }
        }

        binding.editAnswer.setOnEditorActionListener { _, actionId, _ ->
            detailPracticeActivity.setAnswer(binding.editAnswer.text.toString())

            quantity = detailPracticeActivity.getQuantity()

            detailPracticeActivity.replaceOrAddAnswer(binding.editAnswer.text.toString(), quantity)

            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()

                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        binding.constraintLayoutFilling.setOnClickListener {
            hideKeyboard()

            detailPracticeActivity.setAnswer(binding.editAnswer.text.toString())

            quantity = detailPracticeActivity.getQuantity()

            detailPracticeActivity.replaceOrAddAnswer(binding.editAnswer.text.toString(), quantity)
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

    private fun hideKeyboard() {
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        inputMethodManager.hideSoftInputFromWindow(binding.editAnswer.windowToken, 0)
    }
}