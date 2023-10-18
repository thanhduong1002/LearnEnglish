package com.example.learnenglish.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.learnenglish.data.models.DetailVocabulary
import com.example.learnenglish.data.repositories.DetailVocabularyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DetailVocabularyViewModel constructor(private val detailVocabularyRepository: DetailVocabularyRepository): ViewModel() {

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