package com.flowz.introtooralanguage.firebase

import com.flowz.introtooralanguage.firebase.ForgotPasswordActivity
import com.flowz.introtooralanguage.firebase.SignupActivity



import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.flowz.introtooralanguage.MainActivity
import com.flowz.introtooralanguage.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.register.*
import kotlinx.android.synthetic.main.signin.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var databaseReference: DatabaseReference? = null
    private var database: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signin)
        setStatusBarTransparent(this@LoginActivity)

        auth = FirebaseAuth.getInstance()

        login()
    }

    private fun login(){

        user_login.setOnClickListener {

            if (TextUtils.isEmpty(lg_user_email.text.toString())){
                lg_user_email.setError("Please enter your email")
                return@setOnClickListener
            }

           else if (TextUtils.isEmpty(lg_user_password.text.toString())){
                lg_user_email.setError("Please enter your password")
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(lg_user_email.text.toString(), lg_user_password.text.toString())
                .addOnCompleteListener {

                    if (it.isSuccessful){

//                        Toast.makeText(baseContext, "Logged In Successfully", Toast.LENGTH_LONG).show()
                        Snackbar.make(lg_user_email, "Logged In Successfully", Snackbar.LENGTH_LONG).show()


                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }else{

//                        Toast.makeText(baseContext, "User login failed, try again!", Toast.LENGTH_LONG).show()
                        Snackbar.make(lg_user_email, "User login failed, try again!", Snackbar.LENGTH_LONG).show()

                    }
                }
        }

    }

    private fun setStatusBarTransparent(activity: AppCompatActivity){
        //Make Status bar transparent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        //Make status bar icons color dark
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            activity.window.statusBarColor = Color.WHITE
        }
    }

    fun onClick(view: View) {
         if(view.id == R.id.forgotten_password){
            startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
        }
        else if(view.id == R.id.no_account){
            startActivity(Intent(this@LoginActivity, SignupActivity::class.java))
        }
    }
}
