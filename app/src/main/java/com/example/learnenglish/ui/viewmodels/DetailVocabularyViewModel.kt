package com.example.learnenglish.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.learnenglish.data.models.DetailVocabulary
import com.example.learnenglish.data.repositories.DetailVocabularyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DetailVocabularyViewModel constructor(private val detailVocabularyRepository: DetailVocabularyRepository): ViewModel() {
    val _questionList: MutableLiveData<List<String>> = MutableLiveData(emptyList())
    val questionList: LiveData<List<String>> = _questionList

    fun getAllDetailVocabularies(): List<DetailVocabulary> = detailVocabularyRepository.getAll()

    fun getByTopic(topic: String): List<DetailVocabulary> = detailVocabularyRepository.getByTopic(topic)

    fun insertNewVocabulary(newVocabulary: DetailVocabulary) = detailVocabularyRepository.insertNewVocabulary(newVocabulary)

    fun getAllVietnameseWords() = detailVocabularyRepository.getAllVietnameseWords()

    suspend fun getExampleByVietnamese(vietnamese: String): String = withContext(Dispatchers.IO) {
        detailVocabularyRepository.getExampleByVietnamese(vietnamese)
    }

    suspend fun getEnglishByVietnamese(vietnamese: String): String = withContext(Dispatchers.IO) {
        detailVocabularyRepository.getEnglishByVietnamese(vietnamese)
    }

    suspend fun getVietnameseByEnglish(english: String): String = withContext(Dispatchers.IO) {
        detailVocabularyRepository.getVietnameseByEnglish(english)
    }
}

class DetailVocabularyViewModelFactory(private val detailVocabularyRepository: DetailVocabularyRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailVocabularyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailVocabularyViewModel(detailVocabularyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}