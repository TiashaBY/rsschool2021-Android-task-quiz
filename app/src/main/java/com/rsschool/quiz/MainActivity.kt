package com.rsschool.quiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rsschool.quiz.data.Quiz

class MainActivity : AppCompatActivity(), QuizNavListener, QuizResultsListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startQuiz()
    }

    private fun startQuiz() {
        Quiz.initQuestions()
        createFragment(0)
    }

    private fun createFragment(position: Int) {
            val questionFragment = QuestionFragment.newInstance(
                Quiz.questions[position],
                Quiz.questions.size,
                position)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_quiz_container, questionFragment).commit()

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
    }

}
