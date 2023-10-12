package com.example.learnenglish.data.models

import androidx.annotation.DrawableRes
import com.example.learnenglish.R

data class Vocabulary(
    val title: String,

    @DrawableRes val image: Int ?= R.drawable.other,
)