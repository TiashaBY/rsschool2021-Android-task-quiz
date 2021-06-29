package com.rsschool.quiz.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rsschool.quiz.data.Quiz
import com.rsschool.quiz.utils.PageStyleFlags

class QuizAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return Quiz.questions.size + 1
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> QuestionFragment.newInstance(
                Quiz.questions[position],
                Quiz.questions.size,
                PageStyleFlags.FIRST_PAGE
            )
            in 1 until itemCount - 2 -> QuestionFragment.newInstance(
                Quiz.questions[position],
                Quiz.questions.size
            )
            itemCount - 2 -> QuestionFragment.newInstance(
                Quiz.questions[position],
                Quiz.questions.size,
                PageStyleFlags.LAST_PAGE
            )
            else -> ResultsFragment.newInstance(Quiz.questions)
        }
    }
}