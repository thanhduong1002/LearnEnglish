package com.example.learnenglish.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.learnenglish.data.models.DetailVocabulary

@Dao
interface DetailVocabularyDao {
    @Query("SELECT * FROM detailvocabulary")
    fun getAll(): List<DetailVocabulary>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(detailVocabularies: List<DetailVocabulary>)

    @Query("SELECT * FROM detailvocabulary WHERE topic = :topic")
    fun getByTopic(topic: String): List<DetailVocabulary>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewVocabulary(newVocabulary: DetailVocabulary)

    @Query("SELECT vietnamese FROM detailvocabulary")
    fun getAllVietnameseWords(): List<String>

    @Query("SELECT english FROM detailvocabulary")
    fun getAllEnglishesWords(): List<String>

    @Query("SELECT example FROM detailvocabulary WHERE vietnamese = :vietnamese")
    fun getExampleByVietnamese(vietnamese: String): String

    @Query("SELECT english FROM detailvocabulary WHERE vietnamese = :vietnamese")
    fun getEnglishByVietnamese(vietnamese: String): String

    @Query("SELECT vietnamese FROM detailvocabulary WHERE english = :english")
    fun getVietnameseByEnglish(english: String): String

    @Query("SELECT * FROM detailvocabulary ORDER BY RANDOM() LIMIT 1")
    fun getRandomDetailVocabulary(): DetailVocabulary
}