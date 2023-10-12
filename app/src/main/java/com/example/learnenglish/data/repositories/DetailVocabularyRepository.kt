package com.example.learnenglish.data.repositories

import com.example.learnenglish.data.dao.DetailVocabularyDao
import com.example.learnenglish.data.models.DetailVocabulary

class DetailVocabularyRepository constructor(
    private val detailVocabularyDao: DetailVocabularyDao
) {
    fun getAll(): List<DetailVocabulary> = detailVocabularyDao.getAll()

    fun getByTopic(topic: String): List<DetailVocabulary> = detailVocabularyDao.getByTopic(topic)

    fun insertNewVocabulary(newVocabulary: DetailVocabulary) = detailVocabularyDao.insertNewVocabulary(newVocabulary)

    fun getAllVietnameseWords() = detailVocabularyDao.getAllVietnameseWords()
}