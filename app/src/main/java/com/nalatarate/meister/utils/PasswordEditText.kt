package com.nalatarate.meister.utils

import android.content.Context
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet
import android.widget.AutoCompleteTextView

/**
 * Created by tiberiugolaes on 30/05/16.
 */
class PasswordEditText(context: Context, attrs: AttributeSet?) : AppCompatEditText(context, attrs), Valid {
    companion object {
        const val MIN_LENGTH = 8
    }

    override fun isValid(): Boolean = text.length >= MIN_LENGTH
}