package com.example.learnenglish.data.repositories

import com.example.learnenglish.data.dao.DetailVocabularyDao
import com.example.learnenglish.data.models.DetailVocabulary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DetailVocabularyRepository constructor(
    private val detailVocabularyDao: DetailVocabularyDao
) {
    fun getAll(): List<DetailVocabulary> = detailVocabularyDao.getAll()

    fun getByTopic(topic: String): List<DetailVocabulary> = detailVocabularyDao.getByTopic(topic)

    fun insertNewVocabulary(newVocabulary: DetailVocabulary) = detailVocabularyDao.insertNewVocabulary(newVocabulary)

    fun getAllVietnameseWords() = detailVocabularyDao.getAllVietnameseWords()

    fun getExampleByVietnamese(vietnamese: String): String =  detailVocabularyDao.getExampleByVietnamese(vietnamese)

    fun getEnglishByVietnamese(vietnamese: String): String = detailVocabularyDao.getEnglishByVietnamese(vietnamese)

    fun getVietnameseByEnglish(english: String): String = detailVocabularyDao.getVietnameseByEnglish(english)
}