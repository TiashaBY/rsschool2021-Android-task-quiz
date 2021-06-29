package com.rsschool.quiz.ui

import android.R
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.rsschool.quiz.utils.Themes
import com.rsschool.quiz.databinding.FragmentQuizBinding
import com.rsschool.quiz.utils.PageStyleFlags
import com.rsschool.quiz.models.Question


class QuestionFragment : Fragment() {

    private var _binding : FragmentQuizBinding? = null
    private val binding get() = checkNotNull(_binding)

    private var pageStyle: PageStyleFlags? = null
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
        pageStyle = arguments?.getSerializable(PAGE_STYLE).let { it as PageStyleFlags }

        val themeContext = setTheme(Themes.getTheme(activeQuestion?.id!!))
        val localInflater = inflater.cloneInContext(themeContext)
        _binding = FragmentQuizBinding.inflate(localInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayQuestion()
        initListeners()
    }

    override fun onResume() {
        super.onResume()
        val typedValue = activeQuestion?.id?.let { Themes.getTheme(it).id }?.let {
            context?.theme?.obtainStyledAttributes(
                it, intArrayOf(R.attr.statusBarColor))
        }
        activity?.window?.statusBarColor = typedValue?.getColor(0, 0)!!
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        listener = null
    }

    private fun initListeners() {
        binding.nextButton.setOnClickListener {
            listener?.getNextQuestion()
        }

        binding.previousButton.setOnClickListener {
            listener?.getPrevQuestion()
        }
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.optionOne.id ->
                    activeQuestion?.setSelected(0)
                binding.optionTwo.id ->
                    activeQuestion?.setSelected(1)
                binding.optionThree.id ->
                    activeQuestion?.setSelected(2)
                binding.optionFour.id ->
                    activeQuestion?.setSelected(3)
                binding.optionFive.id ->
                    activeQuestion?.setSelected(4)
            }
            binding.nextButton.isEnabled = true
        }

        binding.toolbar.setNavigationOnClickListener {
            listener?.getPrevQuestion()
        }
    }

    private fun setTheme(theme: Themes): ContextThemeWrapper {
        val context = ContextThemeWrapper(context, theme.id)
        getContext()?.theme?.applyStyle(theme.id, true)
        return context
    }

    private fun displayQuestion() {
        binding.question.text = activeQuestion?.question
        binding.toolbar.title = "Question ${activeQuestion?.id} of $amountOfQuestions"
        binding.optionOne.text = activeQuestion?.options?.get(0)
        binding.optionTwo.text = activeQuestion?.options?.get(1)
        binding.optionThree.text = activeQuestion?.options?.get(2)
        binding.optionFour.text = activeQuestion?.options?.get(3)
        binding.optionFive.text = activeQuestion?.options?.get(4)

        //radio
        val selectedAnswer = activeQuestion?.selectedAnswer ?: -1
        if (activeQuestion.let { selectedAnswer >= 0 }) {
            binding.radioGroup.check(binding.radioGroup.getChildAt(selectedAnswer).id)
        } else {
            binding.radioGroup.clearCheck()
            binding.nextButton.isEnabled = false
        }

        //buttons
        when (pageStyle) {
            PageStyleFlags.FIRST_PAGE -> {
                binding.toolbar.navigationIcon = null
                binding.previousButton.visibility = View.INVISIBLE
                binding.nextButton.text = getString(com.rsschool.quiz.R.string.next_button_label)
            }
            PageStyleFlags.LAST_PAGE -> {
                binding.nextButton.text = getString(com.rsschool.quiz.R.string.submit)
            }
             else -> {
                binding.nextButton.text = getString(com.rsschool.quiz.R.string.next_button_label)
                binding.previousButton.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(question:Question, questionsAmount: Int, pageStyle: PageStyleFlags = PageStyleFlags.NORMAL_PAGE): QuestionFragment {
            val fragment = QuestionFragment()
            val args = bundleOf(
                ACTIVE_QUESTION to question,
                QUESTIONS_AMOUNT to questionsAmount,
                PAGE_STYLE to pageStyle
            )
            fragment.arguments = args
            return fragment
        }

        private const val ACTIVE_QUESTION = "ACTIVE_QUESTION"
        private const val QUESTIONS_AMOUNT = "QUESTIONS_AMOUNT"
        private const val PAGE_STYLE = "PAGE_STYLE"
    }
}
