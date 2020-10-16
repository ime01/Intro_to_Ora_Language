package com.flowz.introtooralanguage.display.profile


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation

import com.flowz.introtooralanguage.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_user_profile.*

/**
 * A simple [Fragment] subclass.
 */
class UserProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var databaseReference: DatabaseReference? = null
    private var database: FirebaseDatabase? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")

        loadUserProfile()
    }

    private fun loadUserProfile(){

        val user = auth.currentUser
        val userReference =  databaseReference?.child(user?.uid!!)

        user_profile_email.text = user?.email

        userReference?.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {

                user_profile_name.text = snapshot.child("name").value.toString()
                user_profile_phone_number.text = snapshot.child("phonenumber").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        logout.setOnClickListener {
            auth.signOut()
            val navController : NavController = Navigation.findNavController(view!!)
            navController.navigate(R.id.action_userProfileFragment_to_loginActivity)
        }
    }
}
