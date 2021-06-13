package com.rsschool.quiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rsschool.quiz.data.Quiz
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(), QuizNavListener, QuizResultsListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            startQuiz()
        }
    }

    private fun startQuiz() {
        Quiz.initQuestions()
        createFragment(0)
    }

    private fun createFragment(position: Int) {
        if (position >=0 && position < Quiz.questions.size) {
            val questionFragment = QuestionFragment.newInstance(
                Quiz.questions.get(position),
                Quiz.questions.size,
                position
            )
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_quiz_container, questionFragment).commit()
        }
    }

    override fun sendResult() {
        val resultFragment = ResultsFragment.newInstance(Quiz.questions)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_quiz_container, resultFragment).commit()
    }

    override fun getNextQuestion(currentPosition: Int) {
        createFragment(currentPosition + 1)
    }

    override fun getPrevQuestion(currentPosition: Int) {
        createFragment(currentPosition - 1)
    }

    override fun resetQuiz() {
        startQuiz()
    }

    override fun closeQuiz() {
        finish()
        exitProcess(0)
    }

}
