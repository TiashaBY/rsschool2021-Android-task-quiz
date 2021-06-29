package com.rsschool.quiz.utils

import com.rsschool.quiz.R

enum class Themes(val id: Int, val questionId: Int) {
    QUIZ_FIRST(R.style.Theme_Quiz_First, 1),
    QUIZ_SECOND(R.style.Theme_Quiz_Second, 2),
    QUIZ_THIRD(R.style.Theme_Quiz_Third, 3),
    QUIZ_FOURTH(R.style.Theme_Quiz_Fourth, 4),
    QUIZ_FIFTH(R.style.Theme_Quiz_Fifth, 5);

    companion object {
        fun getTheme(questionId: Int): Themes {
            return values().first { v -> v.questionId == questionId }
        }
    }
}