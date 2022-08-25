package com.kirdevelopment.worship47andorid2.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

interface KeyboardUtils {

    // скрыть клавиатуру для инпута
    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    // показать клавиатуру для инпута
    fun View.showKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

}