package com.nalatarate.meister.utils

import android.text.TextUtils
import android.widget.EditText
import java.util.regex.Pattern

/**
 * Created by tiberiugolaes on 30/05/16.
 */
object EditTextExtensions {
    private val sEmailPattern = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", Pattern.CASE_INSENSITIVE)

    fun EditText.isEmpty(): Boolean {
        return TextUtils.isEmpty(text)
    }

    fun EditText.isValidEmail(): Boolean {
        if (text.isEmpty()) return false
        val matcher = sEmailPattern.matcher(text)
        return matcher.matches()

    }
}