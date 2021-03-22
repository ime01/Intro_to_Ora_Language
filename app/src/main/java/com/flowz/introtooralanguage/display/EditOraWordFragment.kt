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
import com.flowz.introtooralanguage.data.models.HouseWordsModel
import com.flowz.introtooralanguage.data.models.NumbersModel
import com.flowz.introtooralanguage.data.room.OraWordsDatabase
import com.flowz.introtooralanguage.data.models.OutdoorWordsModel
import com.flowz.introtooralanguage.data.models.TravelWordsModel
import com.flowz.introtooralanguage.display.base.ScopedFragment
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
lateinit var numList: ArrayList<NumbersModel>
var searchViewList: ArrayList<NumbersModel> = ArrayList()
//lateinit var oraAdapter: OraNumAdapter
lateinit var uri: Uri
lateinit var selectedPath: Uri
var recordButtonClicked: Boolean = false
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
    private var outdoorWord: OutdoorWordsModel? = null
    private var travelWord: TravelWordsModel? = null
    private var houseWord: HouseWordsModel? = null
    private var number: NumbersModel? = null

    private var  wordFromNumbersFragment :Boolean = false
    private var  wordFromOutDoorFragment :Boolean = false
    private var  wordFromHouseFragment :Boolean = false
    private var  wordFromTravelFragment :Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)

            outdoorWord = EditOraWordFragmentArgs.fromBundle(it).outdoorWord
            travelWord = EditOraWordFragmentArgs.fromBundle(it).travelWord
            houseWord = EditOraWordFragmentArgs.fromBundle(it).houseWord
            number = EditOraWordFragmentArgs.fromBundle(it).number


            if (it.getInt("type") == 1){
//              if equals to 1 that means we came here from here from Numbers Fragment, so we save the updated word with NumbersDao to database
                wordFromNumbersFragment = true

           }else if (it.getInt("type") == 2){
//               if equals to 1 that means we came here from here from House Fragment, so we save the updated word with HouseDao to database
                wordFromHouseFragment = true

           }else if (it.getInt("type") == 3){
//               if equals to 1 that means we came here from here from Travel Fragment, so we save the updated word with TravelDao to database
                wordFromTravelFragment = true

           }else{
//               if equals to 1 that means we came here from here from Outdoor Fragment, so we save the updated word with OutdoorDao to database
                wordFromOutDoorFragment = true
           }


        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_ora_word, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = OraWordsDatabase(requireContext())

        if (wordFromOutDoorFragment==true){

            entered_eng_words.setText(outdoorWord?.engNum)
            entered_ora_words.setText(outdoorWord?.oraNum)

        }else if (wordFromHouseFragment == true){

            entered_eng_words.setText(houseWord?.engNum)
            entered_ora_words.setText(houseWord?.oraNum)

        }else if (wordFromTravelFragment == true){

            entered_eng_words.setText(travelWord?.engNum)
            entered_ora_words.setText(travelWord?.oraNum)

        }else if (wordFromNumbersFragment == true){

            entered_eng_words.setText(number?.engNum)
            entered_ora_words.setText(number?.oraNum)
        }


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
                AUDIO_REQUEST_CODE
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

                    if (wordFromNumbersFragment == true){
                        launch {

                            val updatedOutdoorWord = NumbersModel(engWordEntered, oraWordEntered, null, chosenAudio)

                            updatedOutdoorWord.oraid = number!!.oraid

                            db.NumbersDao().update(updatedOutdoorWord)

                        }
                        showSnackbar(update_oraword, "$engWordEntered has been updated")

                    }else if (wordFromHouseFragment == true){
                        launch {

                            val updatedOutdoorWord = HouseWordsModel(engWordEntered, oraWordEntered,  chosenAudio)

                            updatedOutdoorWord.oraid = houseWord!!.oraid

                            db.houseWordsDao().update(updatedOutdoorWord)

                        }
                        showSnackbar(update_oraword, "$engWordEntered has been updated")

                    }else if(wordFromOutDoorFragment == true){
                        launch {

                            val updatedOutdoorWord = OutdoorWordsModel(engWordEntered, oraWordEntered,  chosenAudio)

                            updatedOutdoorWord.oraid = outdoorWord!!.oraid

                            db.outDoorWordsDao().update(updatedOutdoorWord)

                        }
                        showSnackbar(update_oraword, "$engWordEntered has been updated")

                    }else if(wordFromTravelFragment == true){
                        launch {

                            val updatedOutdoorWord = TravelWordsModel(engWordEntered, oraWordEntered,  chosenAudio)

                            updatedOutdoorWord.oraid = travelWord!!.oraid

                            db.TravelWordsDao().update(updatedOutdoorWord)

                        }
                        showSnackbar(update_oraword, "$engWordEntered has been updated")
                    }

                }
            }
            else if (recordButtonClicked == true){
                if (!entered_eng_words.text.isNullOrBlank() && !entered_ora_words.text.isNullOrBlank()) {

                    val engWordEntered = entered_eng_words.text.toString().trim()
                    val oraWordEntered = entered_ora_words.text.toString().trim()

                    showSnackbar(update_oraword, "strings gotten $engWordEntered $oraWordEntered")

                    if (wordFromNumbersFragment== true){
                        launch {

                            val updatedOutdoorWord = NumbersModel(engWordEntered, oraWordEntered, null, audioUri)

                            updatedOutdoorWord.oraid = number!!.oraid

                            db.NumbersDao().update(updatedOutdoorWord)
                        }

                        showSnackbar(update_oraword, "$engWordEntered has been updated")

                    }else if (wordFromTravelFragment == true){
                        launch {

                            val updatedOutdoorWord = TravelWordsModel(engWordEntered, oraWordEntered, audioUri)

                            updatedOutdoorWord.oraid = travelWord!!.oraid

                            db.TravelWordsDao().update(updatedOutdoorWord)
                        }

                        showSnackbar(update_oraword, "$engWordEntered has been updated")

                    }else if (wordFromOutDoorFragment == true){
                        launch {

                            val updatedOutdoorWord = OutdoorWordsModel(engWordEntered, oraWordEntered, audioUri)

                            updatedOutdoorWord.oraid = outdoorWord!!.oraid

                            db.outDoorWordsDao().update(updatedOutdoorWord)
                        }

                        showSnackbar(update_oraword, "$engWordEntered has been updated")

                    }else if (wordFromHouseFragment == true){
                        launch {

                            val updatedOutdoorWord = HouseWordsModel(engWordEntered, oraWordEntered, audioUri)

                            updatedOutdoorWord.oraid = houseWord!!.oraid

                            db.houseWordsDao().update(updatedOutdoorWord)
                        }

                        showSnackbar(update_oraword, "$engWordEntered has been updated")
                    }

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
                AUDIO_REQUEST_CODE
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
                showToast("Error in getting music file", this.requireContext())
            }

            if (requestCode == AUDIO_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

                if (data != null) {

                    val muri = data.data

                    if (data != null) {

                        val muri = data.data

                        audioFilePath = muri.toString()

//                    playContentUri(muri!!)
                        playContentUri(muri!!, this.requireContext())

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
        showToast("Recording Started", this.requireContext())
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
        showToast("Recording Stoped",this.requireContext() )
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

                    showToast("Record permission required", this.requireContext())
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
                    showToast("External Storage permission required", this.requireContext())
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
            showToast("No microphone to reocrd, try selecting an Audio file instead", this.requireContext())
        } else {
            showToast("Microphone Present", this.requireContext())
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
        const val AUDIO_REQUEST_CODE = 1
        const val KEY_COUNT_VALUE = "key_count"
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

