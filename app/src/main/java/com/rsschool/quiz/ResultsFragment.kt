package com.rsschool.quiz

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.rsschool.quiz.databinding.FragmentResultBinding
import com.rsschool.quiz.models.Question
import java.util.ArrayList


class ResultsFragment : Fragment() {

    private var viewBinding : FragmentResultBinding? = null
    private var listener: QuizResultsListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is QuizResultsListener) {
            listener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                listener?.resetQuiz()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        viewBinding = FragmentResultBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val results = arguments?.getParcelableArrayList<Question>("RESULT_LIST")

        val score = calculateResult(results).toString()
        viewBinding?.score?.text = "Your result: $score%"

        viewBinding?.share?.setOnClickListener {
            sendResults(results)
        }

        viewBinding?.restart?.setOnClickListener{
            listener?.resetQuiz()
        }

        viewBinding?.close?.setOnClickListener{
            listener?.closeQuiz()
        }
    }

    private fun sendResults(results: ArrayList<Question>?) {
        val score = calculateResult(results).toString()
        val emailIntent = Intent(Intent.ACTION_SEND, Uri.parse("mailto:"))
        emailIntent.type = "text/plain"
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "My quiz result")
        emailIntent.putExtra(
            Intent.EXTRA_TEXT, "My score: $score%\n\n"
                    + results?.joinToString("\n\n")
        )
        startActivity(emailIntent)
    }

    private fun calculateResult(results: List<Question>?) : Int {
        return results?.let { it.filter { q -> q.selectedAnswer == q.correctAnswer  }.count() * 100 / results.size } ?: 0
    }

    companion object {
        @JvmStatic
        fun newInstance(questionList : List<Question>): ResultsFragment {
            val fragment = ResultsFragment()
            val args = bundleOf(RESULT_LIST to questionList)
            fragment.arguments = args
            return fragment
        }
        private const val RESULT_LIST = "RESULT_LIST"
    }
}

