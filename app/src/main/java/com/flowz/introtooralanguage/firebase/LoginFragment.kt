//package com.flowz.introtooralanguage.firebase
//
//import android.content.Intent
//import android.os.Bundle
//import android.os.Handler
//import android.text.TextUtils
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import androidx.fragment.app.Fragment
//import androidx.navigation.NavController
//import androidx.navigation.Navigation
//import androidx.navigation.fragment.NavHostFragment
//import com.flowz.introtooralanguage.MainActivity
//import com.flowz.introtooralanguage.R
//import com.flowz.introtooralanguage.extensions.playAnimation
//import com.google.android.material.snackbar.Snackbar
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseUser
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//import kotlinx.android.synthetic.main.signin.*
//import kotlinx.android.synthetic.main.signin.view.*
//
//class LoginFragment: Fragment() {
//    private lateinit var auth: FirebaseAuth
//    private var databaseReference: DatabaseReference? = null
//    private var database: FirebaseDatabase? = null
//    private lateinit var firebaseAuthStateListener : FirebaseAuth.AuthStateListener
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        // Inflate the layout for this fragment
//        val view = inflater.inflate(R.layout.signin, container, false)
////        val navController : NavController = Navigation.findNavController(view)
////
////        auth = FirebaseAuth.getInstance()
////
////        firebaseAuthStateListener = FirebaseAuth.AuthStateListener {
////
////            val oraUser : FirebaseUser? = it.currentUser
////
////            if (oraUser!= null){
////                navController.navigate(R.id.action_loginFragment_to_oraLangHomeFragment)
//////                startActivity(Intent(this, MainActivity::class.java))
//////                Log.e("loginActivity",  oraUser.toString())
//////                finish()
////            }
////        }
////        login(navController)
//        return view
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
////        val oraWelcomePerson = findViewById<ImageView>(R.id.ora_person)
//
//        val navController : NavController = Navigation.findNavController(view)
//
//        val oraWelcomePerson = view.ora_person
//
//        playAnimation(this.requireContext(), R.anim.bounce, oraWelcomePerson)
//
//        auth = FirebaseAuth.getInstance()
//
//        firebaseAuthStateListener = FirebaseAuth.AuthStateListener {
//
//            val oraUser : FirebaseUser? = it.currentUser
//
//            if (oraUser!= null){
//                navController.navigate(R.id.action_loginFragment_to_oraLangHomeFragment)
////                startActivity(Intent(this, MainActivity::class.java))
////                Log.e("loginActivity",  oraUser.toString())
////                finish()
//            }
//        }
//
//        login(navController)
//    }
//
//
//    override fun onResume() {
//        super.onResume()
//        FirebaseAuth.getInstance().addAuthStateListener(firebaseAuthStateListener)
//    }
//
//    override fun onStop() {
//        super.onStop()
//        FirebaseAuth.getInstance().removeAuthStateListener(firebaseAuthStateListener)
//    }
//
//    private fun login(navController: NavController){
//
//        user_login.setOnClickListener {
//
//            if (TextUtils.isEmpty(lg_user_email.text.toString())){
//                lg_user_email.setError("Please enter your email")
//                return@setOnClickListener
//            }
//
//            else if (TextUtils.isEmpty(lg_user_password.text.toString())){
//                lg_user_email.setError("Please enter your password")
//                return@setOnClickListener
//            }
//
//            auth.signInWithEmailAndPassword(lg_user_email.text.toString(), lg_user_password.text.toString())
//                .addOnCompleteListener {
//
//                    if (it.isSuccessful){
//
////                        Toast.makeText(baseContext, "Logged In Successfully", Toast.LENGTH_LONG).show()
//                        Snackbar.make(lg_user_email, "Logged In Successfully", Snackbar.LENGTH_LONG).show()
//                        navController.navigate(R.id.action_loginFragment_to_oraLangHomeFragment)
//
//
////                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
////                        finish()
//                    }else{
//
////                        Toast.makeText(baseContext, "User login failed, try again!", Toast.LENGTH_LONG).show()
//                        Snackbar.make(lg_user_email, "User login failed, try again!", Snackbar.LENGTH_LONG).show()
//
//                    }
//                }
//        }
//    }
//
//
////    fun onClick(view: View) {
////        if(view.id == R.id.forgotten_password){
//////             The line of code below crashes the app, this was done intentionally to test the FIREBASE CRASHLYTICS i just added to the app to give useful info about any crash from users.
//////             throw RuntimeException("Test Crash")
////            navController.navigate(R.id.action_loginFragment_to_oraLangHomeFragment)
////
//////            startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
////        }
////        else if(view.id == R.id.no_account){
////            startActivity(Intent(this@LoginActivity, SignupActivity::class.java))
////        }
////    }
//}