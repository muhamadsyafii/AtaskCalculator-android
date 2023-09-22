/*
 * Created by Muhamad Syafii
 * Friday, 22/9/2023
 * Copyright (c) 2023.
 * All Rights Reserved
 */

package id.syafii.ataskcalculator.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import id.syafii.ataskcalculator.data.Response

fun View.visible() {
    if (this.visibility == View.GONE || this.visibility == View.INVISIBLE) this.visibility =
        View.VISIBLE
}

fun View.gone() {
    if (this.visibility == View.VISIBLE) this.visibility = View.GONE
}

fun View.invisible() {
    if (this.visibility == View.VISIBLE) this.visibility = View.INVISIBLE
}

fun Activity.hideKeyboard() {
    val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = currentFocus
    if (view != null) {
        inputManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}

fun <T> MutableLiveData<T>.toLiveData(): LiveData<T> = this

