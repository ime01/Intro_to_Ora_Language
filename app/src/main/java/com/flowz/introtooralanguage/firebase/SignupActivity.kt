package com.flowz.introtooralanguage.firebase

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.flowz.introtooralanguage.MainActivity
import com.flowz.introtooralanguage.R
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.forgotpassword.*
//import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.register.*
import kotlinx.android.synthetic.main.signin.*
//import kotlinx.android.synthetic.main.signin.user_email

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var databaseReference: DatabaseReference? = null
    private var database: FirebaseDatabase? = null

    lateinit var email : String
    lateinit var password : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)

//        val toolbar = findViewById<Toolbar>(R.id.toolbar)
//        setSupportActionBar(toolbar)
//        supportActionBar?.setDisplayShowTitleEnabled(false)
//        toolbar.setNavigationOnClickListener { super.onBackPressed() }
//        setStatusBarWhite(this@SignupActivity)

//        auth = Firebase.auth
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")


        email = rg_user_email.text.toString()

        val enteredPassword = rg_user_password.text.toString()
        val confirmEnteredPassword = rg_user_confirm_password.text.toString()
        Log.d(Tag, "Email is $email" + "Password is $confirmEnteredPassword")

        if (enteredPassword==confirmEnteredPassword){

            password = confirmEnteredPassword
        }

        register_user.setOnClickListener {

            signUpUser()
        }

        account_holder.setOnClickListener {
            startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
        }


    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val user = auth.currentUser
        updateUI(user)
    }

    private fun updateUI(currentUser: FirebaseUser?) {

    }
    private fun signUpUser() {

        if(rg_user_name.text.toString().isEmpty()){
            rg_user_name.error = "Please enter userName"
            rg_user_name.requestFocus()
            return
        }

        if(user_phone_number.text.toString().isEmpty()){
            rg_user_name.error = "Please enter your phone number"
            rg_user_name.requestFocus()
            return
        }

        if(rg_user_password.text.toString().isEmpty()){
            rg_user_password.error = "Please enter password"
            rg_user_password.requestFocus()
            return
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(rg_user_email.text.toString()).matches()){
            rg_user_email.error = "Please enter valid email"
            rg_user_email.requestFocus()
            return
        }

        if(rg_user_confirm_password.text.toString().isEmpty()){
            rg_user_confirm_password.error = "Please enter password again to confirm"
            rg_user_confirm_password.requestFocus()
            return
        }


        auth.createUserWithEmailAndPassword(rg_user_email.text.toString(), rg_user_confirm_password.text.toString())
            .addOnCompleteListener(this){
                if(it.isSuccessful){

                    val currentUser = auth.currentUser
                    val currentUserDb = databaseReference?.child(currentUser?.uid!!)
                    currentUserDb?.child("name")?.setValue(rg_user_name.text.toString())
                    currentUserDb?.child("phonenumber")?.setValue(user_phone_number.text.toString())

                    Log.d(Tag, "created User with Email Successfully")
//                    Toast.makeText(baseContext, "User account created Successfully", Toast.LENGTH_LONG).show()
                    Snackbar.make(rg_user_name, "User account created Successfully", Snackbar.LENGTH_LONG).show()

                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
//                    updateUI(user)

                }else{

                    Log.w(Tag, "User creation failed", it.exception)
//                    Toast.makeText(baseContext, "Authentication failed, Try again later", Toast.LENGTH_LONG).show()
                    Snackbar.make(button_send_reset_email, "User account creation failed", Snackbar.LENGTH_LONG).show()
//                    updateUI(null)
                }
            }
    }

    companion object {
        const val Tag = "SignUp Activity"
    }

    private fun setStatusBarWhite(activity: AppCompatActivity){
        //Make status bar icons color dark
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            activity.window.statusBarColor = Color.WHITE
        }
    }
}
