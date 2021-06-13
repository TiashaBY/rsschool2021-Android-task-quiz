package com.rsschool.quiz

import com.rsschool.quiz.models.Question

interface QuizNavListener {
    fun sendResult()

    fun getNextQuestion(currentPosition: Int)

    fun getPrevQuestion(currentPosition: Int)
}