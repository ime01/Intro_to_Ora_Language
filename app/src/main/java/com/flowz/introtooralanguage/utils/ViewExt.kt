package com.flowz.introtooralanguage.utils

import android.text.Editable
import androidx.appcompat.widget.SearchView
import com.google.android.material.textfield.TextInputEditText


inline fun SearchView.onQueryTextChanged(crossinline listener:(String)-> Unit){
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
        override fun onQueryTextSubmit(query: String?): Boolean {
           return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            listener(newText.orEmpty())
            return true
        }
    })
}

fun TextInputEditText.takeWords() : String{
    return this.text.toString().trim()
}