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
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.learnenglish.R
import com.example.learnenglish.data.dao.DetailVocabularyDao
import com.example.learnenglish.data.local.database.AppDatabase
import com.example.learnenglish.data.repositories.DetailVocabularyRepository
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_multiple_choice, container, false)
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

        val textViewQuestion: TextView = view.findViewById(R.id.textViewQuestion)
        val recyclerViewAnswer: RecyclerView = view.findViewById(R.id.recyclerViewAnswer)
        val detailPracticeActivity = requireActivity() as DetailPracticeActivity

        answerAdapter = AnswerAdapter(emptyList(), detailPracticeActivity)

        recyclerViewAnswer.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewAnswer.adapter = answerAdapter

        GlobalScope.launch(Dispatchers.IO) {
            listVietnameses = detailVocabularyViewModel.getAllVietnameseWords()

            randomGroup = listVietnameses.shuffled().take(4)

            val random = Random()

            randomIndex = random.nextInt(randomGroup.size)

            val example = detailVocabularyViewModel.getExampleByVietnamese(randomGroup[randomIndex])
            val english = detailVocabularyViewModel.getEnglishByVietnamese(randomGroup[randomIndex])

            detailPracticeActivity.setQuestion(randomGroup[randomIndex])
            detailPracticeActivity.setAnswer("")

            val spannable = highlightText(example, english, R.color.highlight)

            withContext(Dispatchers.Main) {
                answerAdapter.setAnswersList(randomGroup)
                answerAdapter.notifyDataSetChanged()
                textViewQuestion.text = spannable
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

            spannable.setSpan(ForegroundColorSpan(color), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        return spannable
    }
}