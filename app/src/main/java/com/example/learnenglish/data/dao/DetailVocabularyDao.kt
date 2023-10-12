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
}