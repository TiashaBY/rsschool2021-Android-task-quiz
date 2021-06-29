package com.rsschool.quiz.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.rsschool.quiz.data.Quiz
import com.rsschool.quiz.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), QuizNavListener, QuizResultsListener {

    private lateinit var viewPager: ViewPager2
    private var _binding: ActivityMainBinding? = null
    private val binding get() = checkNotNull(_binding)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Quiz.initQuestions()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAdapter()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initAdapter() {
        val adapter = QuizAdapter(this)
        viewPager = binding.viewPager
        viewPager.adapter = adapter
        viewPager.isUserInputEnabled = false
    }

    override fun getNextQuestion() {
        viewPager.currentItem = viewPager.currentItem + 1
    }

    override fun getPrevQuestion() {
        viewPager.currentItem = viewPager.currentItem - 1
    }

    override fun resetQuiz() {
        closeQuiz()
        startActivity(intent)
    }

    override fun closeQuiz() {
        finish()
    }

    override fun onBackPressed() {
        if (viewPager.currentItem == 0 || viewPager.currentItem == viewPager.adapter?.itemCount?.minus(1)) {
            super.onBackPressed()
        } else {
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }
}
