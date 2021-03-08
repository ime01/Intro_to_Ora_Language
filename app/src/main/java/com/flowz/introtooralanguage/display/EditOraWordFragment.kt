package com.flowz.introtooralanguage.display

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.flowz.introtooralanguage.R
import com.flowz.introtooralanguage.data.OraLangNums
import com.flowz.introtooralanguage.data.room.OraWordsDatabase
import com.flowz.introtooralanguage.display.base.ScopedFragment
import com.flowz.introtooralanguage.display.numbers.OraLangNumbersFragment
import com.flowz.introtooralanguage.display.numbers.OraNumberViewModel1
import com.flowz.introtooralanguage.extensions.playContentUri
import com.flowz.introtooralanguage.extensions.showSnackbar
import com.flowz.introtooralanguage.extensions.showToast
import kotlinx.android.synthetic.main.fragment_edit_ora_word.*
import kotlinx.coroutines.launch
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

var isRecording = false
private val RECORD_REQUEST_CODE = 101
private val STORAGE_REQUEST_CODE = 102
var mediaRecorder: MediaRecorder? = null
var mediaPlayer: MediaPlayer? = null
lateinit var audioFilePath: String
lateinit var numList: ArrayList<OraLangNums>
var searchViewList: ArrayList<OraLangNums> = ArrayList()
//lateinit var oraAdapter: OraNumAdapter
lateinit var uri: Uri
lateinit var selectedPath: Uri
var recordButtonClicked: Boolean = false
private lateinit var numberViewModel: OraNumberViewModel1
val addOraWordTag = "addOraWordTag"

/**
 * A simple [Fragment] subclass.
 * Use the [EditOraWordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditOraWordFragment : ScopedFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var param3: Int? = null
    private var oraLangNums: OraLangNums? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)

            oraLangNums = EditOraWordFragmentArgs.fromBundle(it).oraLangNums

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_ora_word, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = OraWordsDatabase(context!!)

        entered_eng_words.setText(oraLangNums?.engNum)
        entered_ora_words.setText(oraLangNums?.oraNum)

        val animation: Animation = AnimationUtils.loadAnimation(context, R.anim.recorder_icon_blink)
        val navController : NavController = Navigation.findNavController(view)


//        audioFilePath = context?.getExternalFilesDir(null)?.absolutePath + "/oraAudio.3gp"


        internal_audio_update.setOnClickListener {
            recordButtonClicked = false
            val audioIntent = Intent()
            audioIntent.setType("audio/*")
            audioIntent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(
                audioIntent,
                OraLangNumbersFragment.AUDIO_REQUEST_CODE
            )
        }

        update_oraword.setOnClickListener {
            val audioUri = Uri.parse(audioFilePath)

            if(recordButtonClicked == false) {


                val chosenAudio = Uri.parse(audioFilePath)

                if (!entered_eng_words.text.isNullOrBlank() && !entered_ora_words.text.isNullOrBlank()) {

                    val engWordEntered = entered_eng_words.text.toString().trim()
                    val oraWordEntered = entered_ora_words.text.toString().trim()

                    showSnackbar(update_oraword, "Words gotten $engWordEntered $oraWordEntered")

                    launch {

                        val updatedOraWord = OraLangNums(engWordEntered, oraWordEntered, null, chosenAudio)

                        updatedOraWord.oraid = oraLangNums!!.oraid

                        db.oraWordsDao().update(updatedOraWord)
                    }
                    showSnackbar(update_oraword, "OraItem has been updated")
                }
            }
            else if (recordButtonClicked == true){
                if (!entered_eng_words.text.isNullOrBlank() && !entered_ora_words.text.isNullOrBlank()) {

                    val engWordEntered = entered_eng_words.text.toString().trim()
                    val oraWordEntered = entered_ora_words.text.toString().trim()

                    showSnackbar(update_oraword, "strings gotten $engWordEntered $oraWordEntered")

                    launch {

                        val updatedOraWord = OraLangNums(engWordEntered, oraWordEntered, null, audioUri )

                        updatedOraWord.oraid = oraLangNums!!.oraid

                        db.oraWordsDao().update(updatedOraWord)
                    }

                    showSnackbar(update_oraword, "OraItem has been updated")
                }

            }
            navController.popBackStack()
        }


            disgard_oraword_update.setOnClickListener {
                navController.popBackStack()
            }


       record_audio_update.setOnClickListener {
            recordIcons.visibility = View.VISIBLE
            recordButtonClicked = true
        }

        internal_audio_update.setOnClickListener {
            recordButtonClicked = false
            val audioIntent = Intent()
            audioIntent.setType("audio/*")
            audioIntent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(audioIntent,
                OraLangNumbersFragment.AUDIO_REQUEST_CODE
            )
        }

        start_recording_update.setOnClickListener {
            audioSetup()
            start_recording_update.setImageResource(R.drawable.ic_mic_stop_24dp)
            initialiseMediaRecorder()
            start_recording_update.startAnimation(animation)
            stop_recording_update.visibility = View.VISIBLE
        }

        stop_recording_update.setOnClickListener {
            stopAudio()
            start_recording_update.clearAnimation()
            start_recording_update.setImageResource(R.drawable.ic_mic_black_24dp)
            stop_recording_update.visibility = View.GONE
            play_recording_update.visibility = View.VISIBLE
            delete_recording_update.visibility = View.VISIBLE
        }

        play_recording_update.setOnClickListener {
            playAudio()
        }
        delete_recording_update.setOnClickListener {
            audioFilePath = " "
            showSnackbar(update_oraword, "Audio disgarded")

        }
    }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

            if (!(resultCode == Activity.RESULT_OK || data != null || data?.data != null)) {
                showToast("Error in getting music file", this.context!!)
            }

            if (requestCode == OraLangNumbersFragment.AUDIO_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

                if (data != null) {

                    val muri = data.data

                    if (data != null) {

                        val muri = data.data

                        audioFilePath = muri.toString()

//                    playContentUri(muri!!)
                        playContentUri(muri!!, this.context!!)

                        super.onActivityResult(requestCode, resultCode, data)

                    }
                }
            }

    }

    fun initialiseMediaRecorder() {

        isRecording = true

        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
//                setOutputFile(audioFilePath)
            setOutputFile(audioFilePath)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            try {
                prepare()

            } catch (e: IOException) {
                Log.e("recorder", "recorder preparation failed")
            }
            start()
        }
        showToast("Recording Started", this.context!!)
    }

    fun stopAudio() {
        if (isRecording) {
            mediaRecorder?.stop()
            mediaRecorder?.release()
            isRecording = false
        } else {
            mediaPlayer?.release()
            mediaPlayer = null
        }
        showToast("Recording Stoped",this.context!! )
    }


    private fun requestPermission(permissionType: String, requestCode: Int) {

        val permission = ContextCompat.checkSelfPermission(this.requireActivity(), permissionType)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this.requireActivity(),
                arrayOf(permissionType),
                requestCode
            )
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        when (requestCode) {
            RECORD_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    showToast("Record permission required", this.context!!)
                } else {
                    requestPermission(
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        STORAGE_REQUEST_CODE
                    )
                }
                return
            }

            STORAGE_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showToast("External Storage permission required", this.context!!)
                }
                return
            }
        }
    }

    fun playAudio() {
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setDataSource(audioFilePath)
        mediaPlayer?.prepare()
        mediaPlayer?.start()
    }

    fun audioSetup() {
        if (!hasMicrophone()) {
            showToast("No microphone to reocrd, try selecting an Audio file instead", this.context!!)
        } else {
            showToast("Microphone Present", this.context!!)
        }
//        audioFilePath = Environment.getExternalStorageDirectory().absolutePath + "/oraAudio.3gp"
        audioFilePath = context?.getExternalFilesDir(null)?.absolutePath + "/oraAudio.3gp"

        requestPermission(android.Manifest.permission.RECORD_AUDIO, RECORD_REQUEST_CODE)
    }

    fun hasMicrophone(): Boolean {
        val pmanager = this.activity?.packageManager
        return pmanager!!.hasSystemFeature(PackageManager.FEATURE_MICROPHONE)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditOraWordFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditOraWordFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
//                    putString("engWord", param1)
//                    putString("oraWord", param2)

                }
            }
    }
}