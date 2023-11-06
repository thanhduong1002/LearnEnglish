package com.example.learnenglish.ui.fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.learnenglish.data.dao.DetailVocabularyDao
import com.example.learnenglish.data.local.database.AppDatabase
import com.example.learnenglish.data.repositories.DetailVocabularyRepository
import com.example.learnenglish.databinding.FragmentListenAndChooseBinding
import com.example.learnenglish.interfaces.IChooseAnswer
import com.example.learnenglish.ui.activities.DetailListeningActivity
import com.example.learnenglish.ui.adapters.AnswerAdapter
import com.example.learnenglish.ui.viewmodels.DetailVocabularyViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import java.util.Random

class ListenAndChooseFragment : Fragment(), IChooseAnswer {
    private lateinit var binding: FragmentListenAndChooseBinding
    private lateinit var database: AppDatabase
    private lateinit var detailVocabularyDao: DetailVocabularyDao
    private lateinit var detailVocabularyRepository: DetailVocabularyRepository
    private lateinit var detailVocabularyViewModel: DetailVocabularyViewModel
    private lateinit var detailListeningActivity: DetailListeningActivity
    private lateinit var answerAdapter: AnswerAdapter
    private lateinit var listEnglishes: List<String>
    private lateinit var listVietnameses: List<String>
    private lateinit var textForSpeech: String
    private lateinit var textToSpeech: TextToSpeech

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListenAndChooseBinding.inflate(inflater, container, false)
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

        detailListeningActivity = requireActivity() as DetailListeningActivity

        answerAdapter = AnswerAdapter(emptyList())
        binding.recyclerViewAnswer.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewAnswer.adapter = answerAdapter

        detailListeningActivity.setAnswer("")

        GlobalScope.launch(Dispatchers.IO) {
            listEnglishes = detailVocabularyViewModel.getAllEnglishesWords()
            listVietnameses = detailVocabularyViewModel.getAllVietnameseWords()

            val listEnglishToRemove = detailListeningActivity.getListQuestions().toList()
            val newListEnglishes = listEnglishes.filter { englishWord ->
                !listEnglishToRemove.any { englishWordToRemove ->
                    englishWord == englishWordToRemove
                }
            }
            val random = Random()
            val randomIndex = random.nextInt(newListEnglishes.size)
            val english = newListEnglishes[randomIndex]
            val vietnamese = detailVocabularyViewModel.getVietnameseByEnglish(english)
            var newListVietnameses = listVietnameses.shuffled().take(3)

            newListVietnameses = newListVietnameses + vietnamese

            textForSpeech = english

            detailListeningActivity.setQuestion(vietnamese)
            detailListeningActivity.addNewQuestion(english)
            detailListeningActivity.setAnswer("")

            withContext(Dispatchers.Main) {
                answerAdapter.setAnswersList(newListVietnameses, this@ListenAndChooseFragment)
                answerAdapter.notifyDataSetChanged()
            }
        }

        binding.speakerButton.setOnClickListener {
            handleSpeaker(textForSpeech)
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

    override fun onClickAnswer(newAnswer: String) {
        detailListeningActivity.setAnswer(newAnswer)
    }

    override fun getQuantity(): Int {
        return detailListeningActivity.getQuantity()
    }

    override fun replaceOrAddAnswer(newAnswer: String, number: Int) {
        detailListeningActivity.replaceOrAddAnswer(newAnswer, number)
    }
}