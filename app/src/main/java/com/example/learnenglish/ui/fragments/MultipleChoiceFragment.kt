package com.example.learnenglish.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.learnenglish.R
import com.example.learnenglish.data.dao.DetailVocabularyDao
import com.example.learnenglish.data.local.database.AppDatabase
import com.example.learnenglish.data.repositories.DetailVocabularyRepository
import com.example.learnenglish.ui.adapters.AnswerAdapter
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MultipleChoiceFragment : Fragment() {
    private lateinit var answerAdapter: AnswerAdapter
    private lateinit var database: AppDatabase
    private lateinit var detailVocabularyDao: DetailVocabularyDao
    private lateinit var detailVocabularyRepository: DetailVocabularyRepository
    private lateinit var listVietnameses: List<String>
    private lateinit var randomGroup: List<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_multiple_choice, container, false)
    }

    @DelicateCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = AppDatabase.getDatabase(requireContext())
        detailVocabularyDao = database.detailVocabularyDao()
        detailVocabularyRepository = DetailVocabularyRepository(detailVocabularyDao)

        val recyclerViewAnswer: RecyclerView = view.findViewById(R.id.recyclerViewAnswer)

        answerAdapter = AnswerAdapter(emptyList())
        recyclerViewAnswer.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewAnswer.adapter = answerAdapter

        GlobalScope.launch(Dispatchers.IO) {
            listVietnameses = detailVocabularyRepository.getAllVietnameseWords()
            randomGroup= listVietnameses.shuffled().take(4)
            answerAdapter.setCartsList(randomGroup)
            Log.d("list", randomGroup.size.toString())
        }
    }
}