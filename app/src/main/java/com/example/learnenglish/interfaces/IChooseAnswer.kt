package com.example.learnenglish.interfaces

interface IChooseAnswer {
    fun onClickAnswer(newAnswer: String)
    fun getQuantity() : Int
    fun replaceOrAddAnswer(newAnswer: String, number: Int)
}