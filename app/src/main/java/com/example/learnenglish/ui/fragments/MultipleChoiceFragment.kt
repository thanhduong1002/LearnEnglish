package com.example.learnenglish.ui.fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.learnenglish.R
import com.example.learnenglish.data.dao.DetailVocabularyDao
import com.example.learnenglish.data.local.database.AppDatabase
import com.example.learnenglish.data.repositories.DetailVocabularyRepository
import com.example.learnenglish.databinding.FragmentMultipleChoiceBinding
import com.example.learnenglish.ui.activities.DetailPracticeActivity
import com.example.learnenglish.ui.adapters.AnswerAdapter
import com.example.learnenglish.ui.viewmodels.DetailVocabularyViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Random

class MultipleChoiceFragment : Fragment() {
    private lateinit var answerAdapter: AnswerAdapter
    private lateinit var database: AppDatabase
    private lateinit var detailVocabularyDao: DetailVocabularyDao
    private lateinit var detailVocabularyRepository: DetailVocabularyRepository
    private lateinit var detailVocabularyViewModel: DetailVocabularyViewModel
    private lateinit var listVietnameses: List<String>
    private lateinit var randomGroup: List<String>
    private var randomIndex: Int = 0
    private lateinit var binding: FragmentMultipleChoiceBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMultipleChoiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.N)
    @DelicateCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = AppDatabase.getDatabase(requireContext())
        detailVocabularyDao = database.detailVocabularyDao()
        detailVocabularyRepository = DetailVocabularyRepository(detailVocabularyDao)
        detailVocabularyViewModel = DetailVocabularyViewModel(detailVocabularyRepository)

        val detailPracticeActivity = requireActivity() as DetailPracticeActivity

        answerAdapter = AnswerAdapter(emptyList(), detailPracticeActivity)
        binding.recyclerViewAnswer.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewAnswer.adapter = answerAdapter

        GlobalScope.launch(Dispatchers.IO) {
            listVietnameses = detailVocabularyViewModel.getAllVietnameseWords()

            val listEnglishToRemove = detailPracticeActivity.getListQuestions().toList()
            val newListVietnameses = listVietnameses.filter { vietnameseWord ->
                !listEnglishToRemove.any { englishWord ->
                    detailVocabularyViewModel.getEnglishByVietnamese(vietnameseWord) == englishWord
                }
            }
            val random = Random()

            randomGroup = newListVietnameses.shuffled().take(4)
            randomIndex = random.nextInt(randomGroup.size)

            val example = detailVocabularyViewModel.getExampleByVietnamese(randomGroup[randomIndex])
            val english = detailVocabularyViewModel.getEnglishByVietnamese(randomGroup[randomIndex])
            val spannable = highlightText(example, english, R.color.main)

            detailPracticeActivity.setQuestion(randomGroup[randomIndex])
            detailPracticeActivity.addNewQuestion(english)
            detailPracticeActivity.setAnswer("")

            withContext(Dispatchers.Main) {
                answerAdapter.setAnswersList(randomGroup)
                answerAdapter.notifyDataSetChanged()
                binding.textViewQuestion.text = spannable
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun highlightText(text: String, wordToHighlight: String, color: Int): SpannableString {
        val spannable = SpannableString(text)
        val regex = "(?i)\\Q$wordToHighlight\\E".toRegex()
        val matches = regex.findAll(text)

        for (match in matches) {
            val startIndex = match.range.first
            val endIndex = match.range.last + 1

            spannable.setSpan(
                ForegroundColorSpan(color),
                startIndex,
                endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        return spannable
    }
}