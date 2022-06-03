package com.tossdesu.shoppinglist.presentation

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.tossdesu.shoppinglist.R

interface OnChangeTextListener {

    fun changeListener()
}

@BindingAdapter("errorInputName")
fun bindErrorInputName(tilName: TextInputLayout, isError: Boolean) {
    val message = if (isError) {
        tilName.context.getString(R.string.input_name_error)
    } else {
        null
    }
    tilName.error = message
}

@BindingAdapter("errorInputCount")
fun bindErrorInputCount(tilCount: TextInputLayout, isError: Boolean) {
    val message = if (isError) {
        tilCount.context.getString(R.string.input_count_error)
    } else {
        null
    }
    tilCount.error = message
}

@BindingAdapter("textChangedListener")
fun bindTextChangeListener(editText: TextInputEditText, listener: OnChangeTextListener) {
    editText.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            listener.changeListener()
        }

        override fun afterTextChanged(s: Editable?) {
        }
    })
}