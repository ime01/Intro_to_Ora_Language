package com.flowz.introtooralanguage.extensions

import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

/**
 * @author robin
 * Created on 10/11/20
 */
fun AppCompatActivity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun TextInputEditText.takeWords() : String{
    return this.text.toString().trim()
}
fun EditText.takeWords() : String{
    return this.text.toString().trim()
}

fun playAnimation(context:Context, int: Int, view:View){

    val animation = AnimationUtils.loadAnimation(context, int)
    view.startAnimation(animation)
}

fun showToast(string: String, context: Context){

    Toast.makeText(context, string, Toast.LENGTH_LONG).show()
}

fun showSnackbar(view: View, string: String){

    Snackbar.make(view, string, Snackbar.LENGTH_LONG).show()
}