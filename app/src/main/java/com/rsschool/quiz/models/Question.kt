package com.rsschool.quiz.models

import android.os.Parcel
import android.os.Parcelable
import java.util.*

class Question(val id: Int, val question: String?, val options: ArrayList<String>?, val correctAnswer: Int, val themeId: Int) : Parcelable {

    var selectedAnswer: Int = -1

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.readInt(),
        parcel.readInt()
    ) {
        selectedAnswer = parcel.readInt()
    }

    fun setSelected(index: Int) {
        selectedAnswer = index
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest!!.writeStringArray(arrayOf(toString()))
    }

    override fun toString(): String {
        val answer = if (selectedAnswer > -1) {
            options?.get(selectedAnswer)
        } else {
            "NONE"
        }
        return "$id)$question \nYour answer: $answer"
    }

    companion object CREATOR : Parcelable.Creator<Question> {
        override fun createFromParcel(parcel: Parcel): Question {
            return Question(parcel)
        }

        override fun newArray(size: Int): Array<Question?> {
            return arrayOfNulls(size)
        }
    }
}