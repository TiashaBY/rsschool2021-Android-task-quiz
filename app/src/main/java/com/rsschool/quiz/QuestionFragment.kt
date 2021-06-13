package com.rsschool.quiz

import android.R
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.rsschool.quiz.databinding.FragmentQuizBinding
import com.rsschool.quiz.models.Question


class QuestionFragment : Fragment() {

    private var viewBinding : FragmentQuizBinding? = null

    private var currentPosition : Int = -1
    private var activeQuestion : Question? = null
    private var amountOfQuestions : Int = 0
    private var listener: QuizNavListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is QuizNavListener) {
            listener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
            activeQuestion = arguments?.getParcelable(ACTIVE_QUESTION)
            amountOfQuestions = arguments?.getInt(QUESTIONS_AMOUNT) ?: 0
            currentPosition = arguments?.getInt(QUESTION_NUMBER) ?: -1

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                listener?.getPrevQuestion(currentPosition)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        val themeContext = setTheme(activeQuestion?.themeId!!)
        val localInflater = inflater.cloneInContext(themeContext)
        viewBinding = FragmentQuizBinding.inflate(localInflater, container, false)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayQuestion()

        viewBinding?.nextButton?.setOnClickListener {
            if (currentPosition == (amountOfQuestions - 1)) {
                listener?.sendResult()
            } else {
                listener?.getNextQuestion(currentPosition)
            }
        }

        viewBinding?.previousButton?.setOnClickListener {
            listener?.getPrevQuestion(currentPosition)
        }
        viewBinding?.radioGroup?.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                viewBinding?.optionOne?.id ->
                    activeQuestion?.setSelected(0)
                viewBinding?.optionTwo?.id ->
                    activeQuestion?.setSelected(1)
                viewBinding?.optionThree?.id ->
                    activeQuestion?.setSelected(2)
                viewBinding?.optionFour?.id ->
                    activeQuestion?.setSelected(3)
                viewBinding?.optionFive?.id ->
                    activeQuestion?.setSelected(4)
            }
            viewBinding?.nextButton?.isEnabled = true
        }

        viewBinding?.toolbar?.setNavigationOnClickListener {
            listener?.getPrevQuestion(currentPosition)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }

    private fun setTheme(themeId: Int): ContextThemeWrapper {
        val context = ContextThemeWrapper(context, activeQuestion?.themeId!!)
        val typedValue = context.theme?.obtainStyledAttributes(
            themeId,
            intArrayOf(R.attr.statusBarColor)
        )
        activity?.window?.statusBarColor = typedValue?.getColor(0, 0)!!
        return context
    }

    private fun displayQuestion() {
        viewBinding?.question?.text = activeQuestion?.question
        viewBinding?.toolbar?.title = "Question ${currentPosition+1} of $amountOfQuestions"
        viewBinding?.optionOne?.text = activeQuestion?.options?.get(0)
        viewBinding?.optionTwo?.text = activeQuestion?.options?.get(1)
        viewBinding?.optionThree?.text = activeQuestion?.options?.get(2)
        viewBinding?.optionFour?.text = activeQuestion?.options?.get(3)
        viewBinding?.optionFive?.text = activeQuestion?.options?.get(4)

        //radio
        val selectedAnswer = activeQuestion?.selectedAnswer ?: -1
        if (activeQuestion.let { selectedAnswer >= 0 }) {
            viewBinding?.radioGroup?.getChildAt(selectedAnswer)?.id?.let {
                viewBinding?.radioGroup?.check(it)
            }
        } else {
            viewBinding?.radioGroup?.clearCheck()
            viewBinding?.nextButton?.isEnabled = false
        }

        //buttons
        when {
            currentPosition <=0 -> {
                viewBinding?.toolbar?.navigationIcon = null
                viewBinding?.previousButton?.visibility = View.INVISIBLE
                viewBinding?.nextButton?.text = getString(com.rsschool.quiz.R.string.next_button_label)
            }
            currentPosition == (amountOfQuestions - 1) -> {
                viewBinding?.nextButton?.text = getString(com.rsschool.quiz.R.string.submit)
            }
            else -> {
                viewBinding?.nextButton?.text = getString(com.rsschool.quiz.R.string.next_button_label)
                viewBinding?.previousButton?.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(question :Question, questionsAmount: Int, questionNumber : Int): QuestionFragment {
            val fragment = QuestionFragment()
            val args = bundleOf(ACTIVE_QUESTION to question,
                QUESTIONS_AMOUNT to questionsAmount,
                QUESTION_NUMBER to questionNumber
            )
            fragment.arguments = args
            return fragment
        }

        private const val ACTIVE_QUESTION = "ACTIVE_QUESTION"
        private const val QUESTIONS_AMOUNT = "QUESTIONS_AMOUNT"
        private const val QUESTION_NUMBER = "QUESTION_NUMBER"
    }
}
