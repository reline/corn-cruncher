package com.github.reline.corncruncher

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

fun Activity.hideKeyboard() {
	currentFocus?.windowToken?.let {
		val context = applicationContext?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
		context?.hideSoftInputFromWindow(it, InputMethodManager.HIDE_NOT_ALWAYS)
	}
}

// todo: lambda?
//public actual inline fun String.toFloatSafely(): Float = try

inline fun <T: Any> ifLet(vararg elements: T?, closure: (List<T>) -> Unit) {
	if (elements.all { it != null }) {
		closure(elements.filterNotNull())
	}
}