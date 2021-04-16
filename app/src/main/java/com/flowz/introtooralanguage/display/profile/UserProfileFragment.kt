package com.flowz.introtooralanguage.display.profile


import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation

import com.flowz.introtooralanguage.R
import com.flowz.introtooralanguage.extensions.showSnackbar
import com.flowz.introtooralanguage.extensions.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_user_profile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class UserProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var databaseReference: DatabaseReference? = null
    private var database: FirebaseDatabase? = null
    private val RequestCode = 101
    private var imageUri : Uri? = null
//    private var storageRef: DatabaseReference?  = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")

        val currentUser = auth.currentUser

           loadUserProfile()

        user_profile_picture.setOnClickListener {
            checkPermssion()
            saveImagetoFirebaseStorage(currentUser!!)
//            pickImage()
//            uploadOraUserImage()
            showSnackbar(user_profile_picture, "Profile Image Updated")
        }
        add_image_icon.setOnClickListener {
//            pickImage()
//            uploadOraUserImage()
        }

        update_ora_profile.setOnClickListener {
            uploadOraUserImage()
        }
    }


    fun checkPermssion(){
        if(Build.VERSION.SDK_INT>=23){
            if (ActivityCompat.checkSelfPermission(this.requireActivity()
                ,android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), READIMAGE)

                return

                }
        }
        pickImage()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
       when(requestCode){
           READIMAGE->{
               if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                   pickImage()
               }else{
                   showToast("Cannnot access your images",this.requireContext() )
               }
           }else-> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
       }


    }

//    private fun loadImage() {
//
//    }

    companion object{
        val READIMAGE = 253
    }


    private fun uploadOraUserImage() {
        auth.currentUser?.let {user->
            val photoURI = imageUri
            val userName = user_profile_name.text.toString()
            val userEmail = user_profile_email.text.toString()
            val userPhoneNo = user_profile_phone_number.text.toString()
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(userName)
                .setPhotoUri(photoURI)
                .build()

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    user.updateProfile(profileUpdates).isSuccessful
                    withContext(Dispatchers.Main){
                        loadUserProfile()
                        showSnackbar(user_profile_picture, "Successfully updated user profile")
                    }
                }catch (e:Exception){
                    withContext(Dispatchers.Main){
                        e.message?.let { it1 -> showSnackbar(user_profile_picture, it1) }
                    }
                }
            }

        }
    }

    private fun pickImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, RequestCode )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RequestCode && resultCode == Activity.RESULT_OK && data!!.data != null ){

            imageUri = data.data
            showSnackbar(user_profile_picture, "Profile picture selected for upload....")

        }
    }

    fun saveImagetoFirebaseStorage( currentUser: FirebaseUser){
        val storage = FirebaseStorage.getInstance()
        val email = currentUser.email
        val storageRef = storage.getReferenceFromUrl("gs://introtooralanguage.appspot.com")
        val df = SimpleDateFormat("ddMMyyHHmmss")
        val dataobj = Date()
        val imagePath = SplitString(email!!) + "," + df.format(dataobj) + ".jpg"
        val imageRef = storageRef.child("images/" + imagePath)

        val uploadTask = imageUri?.let { imageRef.putFile(it) }
        uploadTask?.addOnFailureListener{
           showSnackbar(user_profile_picture, "Failed to upload profile pic")
        }?.addOnSuccessListener {taskSnapshot ->
            var downloadUrl = storageRef.downloadUrl.addOnSuccessListener {
                showToast(it.toString(), this.requireContext())
                user_profile_picture.setImageURI(it)
            }

        }


    }

    fun SplitString(email:String):String{
        val split = email.split("@")
        return split[0]
    }
    private fun loadUserProfile(){

        val user = auth.currentUser
        val userReference =  databaseReference?.child(user?.uid!!)


        userReference?.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {

                user_profile_name.setText(snapshot.child("name").value.toString())
                user_profile_phone_number.setText(snapshot.child("phonenumber").value.toString())
                user_profile_email.setText(user?.email)
                user_profile_picture.setImageURI(user?.photoUrl)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        logout.setOnClickListener {
            auth.signOut()
            val navController : NavController = Navigation.findNavController(requireView())
            navController.navigate(R.id.action_userProfileFragment_to_loginActivity)
        }
    }
}
