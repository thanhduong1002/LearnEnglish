package com.example.learnenglish.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DetailVocabulary(
    @PrimaryKey
    @ColumnInfo(name = "english")
    var english: String,

    @ColumnInfo(name = "vietnamese")
    var vietnamese: String,

    @ColumnInfo(name = "spelling")
    var spelling: String,

    @ColumnInfo(name = "example")
    var example: String,

    @ColumnInfo(name = "exampleVN")
    var exampleVN: String,

    @ColumnInfo(name = "topic")
    var topic: String,

)

