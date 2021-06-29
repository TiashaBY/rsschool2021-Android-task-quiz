package com.rsschool.quiz.data

import com.rsschool.quiz.models.Question

object Quiz {
    var questions : ArrayList<Question> = getDefaults()
        private set

    fun initQuestions() {
        questions = getDefaults()
    }
    private fun getDefaults() : ArrayList<Question> {
        val q1 = Question(1, "Which is the only edible food that never goes bad?", arrayListOf("Rice", "Honey", "Meat", "Sugar", "Vodka"), 1)
        val q2 = Question(2, "What is Earth's largest continent?", arrayListOf("Asia", "Africa", "America", "Europe", "Australia"), 0)
        val q3 = Question(3, "What is the seventh planet from the sun?", arrayListOf("Earth", "Saturn", "Mercury", "Venus", "Uranus"), 4)
        val q4 = Question(4, "What nut is used to make marzipan?", arrayListOf("Chestnuts", "Hazel", "Cashews", "Almond", "Pistachios"), 3)
        val q5 = Question(5, "What is the color of an emerald?", arrayListOf("White", "Blue", "Green", "Orange", "Red"), 2)
        return arrayListOf(q1, q2, q3, q4, q5)
    }
}

