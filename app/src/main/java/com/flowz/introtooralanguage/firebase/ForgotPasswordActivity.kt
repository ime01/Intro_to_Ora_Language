package com.flowz.introtooralanguage.firebase

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.flowz.introtooralanguage.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.forgotpassword.*

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var databaseReference: DatabaseReference? = null
    private var database: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgotpassword)

//        val toolbar = findViewById<Toolbar>(R.id.toolbar)
//        setSupportActionBar(toolbar)
//        supportActionBar?.setDisplayShowTitleEnabled(false)
//        toolbar.setNavigationOnClickListener { super.onBackPressed() }
//        setStatusBarWhite(this@ForgotPasswordActivity)

        auth = FirebaseAuth.getInstance()

        resetUserEmail()
    }
    fun setStatusBarWhite(activity: AppCompatActivity){
        //Make status bar icons color dark
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            activity.window.statusBarColor = Color.WHITE
        }
    }

    fun resetUserEmail(){

        button_send_reset_email.setOnClickListener {

            auth.sendPasswordResetEmail(user_entered_reset_email.text.toString())
                .addOnCompleteListener {

                    if (it.isSuccessful){

                        Snackbar.make(button_send_reset_email, "Reset Email Sent to your account", Snackbar.LENGTH_LONG).show()
                    }
                }

                .addOnFailureListener {

                    Snackbar.make(button_send_reset_email, "Failed to Send Reset Email, Try again", Snackbar.LENGTH_LONG).show()

                }
        }
    }
}
