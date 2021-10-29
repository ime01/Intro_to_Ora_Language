package com.flowz.introtooralanguage.display.profile


import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.flowz.introtooralanguage.R
import com.flowz.introtooralanguage.extensions.showSnackbar
import com.flowz.introtooralanguage.extensions.showToast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_user_profile.*
import kotlinx.android.synthetic.main.ora_num.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class UserProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var databaseReference: DatabaseReference? = null
    private var database: FirebaseDatabase? = null
    private var imageUri : Uri? = null
    private var currentUser : FirebaseUser? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")

        currentUser = auth.currentUser

        loadUserProfile()
        getProfilePicture()

        val getUserImageFromGallery = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                imageUri = it
                showSnackbar(user_profile_picture, "Profile picture chosen....")

                saveImagetoFirebaseStorage(currentUser!!)
            }
        )

        user_profile_picture.setOnClickListener{

            checkPermssion()
//            pickImage()
            getUserImageFromGallery.launch("image/*")


        }
        add_image_icon.setOnClickListener {
            checkPermssion()
            getUserImageFromGallery.launch("image/*")
//
        }

        update_ora_profile.setOnClickListener {
            updateUserInformation()

        }


    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
       when(requestCode){
           READIMAGE->{
               if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
//                   pickImage()
               }else{
                   showToast("Cannnot access your images", requireContext() )
               }
           }else-> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
       }


    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == REQUESTCODE && resultCode == Activity.RESULT_OK && data!!.data != null ){
//
//            imageUri = data.data
//            showSnackbar(user_profile_picture, "Profile picture chosen....")
//
//            saveImagetoFirebaseStorage(currentUser!!)
//
//        }
//    }

    fun checkPermssion(){
        if(Build.VERSION.SDK_INT>=23){
            if (ActivityCompat.checkSelfPermission(this.requireActivity()
                    ,android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), READIMAGE)
                showToast("Check Permission clicked", requireContext() )
                return
            }
        }
    }

//    private fun pickImage() {
//        val intent = Intent()
//        intent.type = "image/*"
//        intent.action = Intent.ACTION_GET_CONTENT
//        startActivityForResult(intent, REQUESTCODE )
//    }


    fun saveImagetoFirebaseStorage( currentUser: FirebaseUser) {

        user_profile_progressBar.visibility = View.VISIBLE

        val storage = FirebaseStorage.getInstance()
        val email = currentUser.email
        val storageRef = storage.getReference("images/"+auth.currentUser?.uid+".jpg")
//        val df = SimpleDateFormat("ddMMyyHHmmss")
//        val dataobj = Date()
//        val imagePath = SplitString(email!!) + "," + df.format(dataobj) + ".jpg"
//        val imageRef = storageRef.child("images/"+auth.currentUser?.uid+".jpg")

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {


            val uploadTask = imageUri?.let {
                storageRef.putFile(it)
            }

            val urlTask = uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation storageRef.downloadUrl
            })?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    user_profile_progressBar.visibility = View.INVISIBLE
                    showToast("Image Uploaded Successfully ", requireContext())
//
                        Picasso.get().load(downloadUri).placeholder(R.drawable.ic_baseline_person_24).error(R.drawable.ic_baseline_person_24).into(user_profile_picture)

                } else {
                    showToast("Image Uploaded Failed ", requireContext())
                }
            }?.addOnFailureListener{
                user_profile_progressBar.visibility = View.INVISIBLE
                showToast("Image Uploaded Failed ", requireContext())

            }

        }

    }

    fun SplitString(email:String):String{
        val split = email.split("@")
        return split[0]
    }

    private fun getProfilePicture(){
        user_profile_progressBar.visibility = View.VISIBLE
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child("images/${auth.currentUser?.uid}.jpg")
        val localFile = File.createTempFile("tempImage", "jpg")

        storageRef.getFile(localFile).addOnSuccessListener {

            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            user_profile_picture.setImageBitmap(bitmap)
            user_profile_progressBar.visibility = View.INVISIBLE

        }.addOnFailureListener{
            user_profile_progressBar.visibility = View.INVISIBLE
            showToast("Failed to get Profile Image", requireContext())
        }

    }

    private fun updateUserInformation() {

        auth.currentUser?.let {user->
            val photoURI = imageUri
            val userName = user_profile_name.text.toString()
            val userEmail = user_profile_email.text.toString()
            val userPhoneNo = user_profile_phone_number.text.toString()

            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(userName)
                .build()

            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main){
                try {

                    val phoneAuthCredential = PhoneAuthProvider.getCredential(userPhoneNo, "OTP_CODE")
                    auth.getCurrentUser()?.updatePhoneNumber(phoneAuthCredential)
                        ?.addOnCompleteListener(OnCompleteListener<Void?> { task ->
                            if (task.isSuccessful) {
                                // Update Successfully
                            } else {
                                // Failed
                            }
                        }
                        )
                    user.updatePhoneNumber(phoneAuthCredential)
                    user.updateEmail(userEmail)
                    user.updateProfile(profileUpdates).isSuccessful

                    withContext(Dispatchers.Main){
                        loadUserProfile()
                        showSnackbar(user_profile_picture, "Successfully updated user profile information")
                    }
                }catch (e:Exception){
                    withContext(Dispatchers.Main){
                        e.message?.let { it1 -> showSnackbar(user_profile_picture, it1) }
                    }
                }
            }

        }
    }

    private fun loadUserProfile(){

        val userReference =  databaseReference?.child(currentUser?.uid!!)


        userReference?.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {

                user_profile_name.setText(snapshot.child("name").value.toString())
                user_profile_phone_number.setText(snapshot.child("phonenumber").value.toString())
                user_profile_email.setText(currentUser?.email)
            }

            override fun onCancelled(error: DatabaseError) {
                showToast("Failed to Load User Profile Data", requireContext())

            }

        })

        logout.setOnClickListener {
            auth.signOut()
            val navController : NavController = Navigation.findNavController(requireView())
            navController.navigate(R.id.action_userProfileFragment_to_loginActivity)
        }
    }

    companion object{
        val READIMAGE = 253
        val REQUESTCODE = 101
    }
}
