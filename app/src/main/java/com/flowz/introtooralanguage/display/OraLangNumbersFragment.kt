package com.flowz.introtooralanguage.display


import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager

import com.flowz.introtooralanguage.R
import com.flowz.introtooralanguage.adapters.OraNumAdapter
import com.flowz.introtooralanguage.recyclerviewlistener.RecyclerItemClickListener
import com.flowz.introtooralanguage.data.OraLangNums
import com.flowz.introtooralanguage.data.numList
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.alert_dialog.*
import kotlinx.android.synthetic.main.ora_lang_numbers.*
import java.io.File
import java.io.IOException
import java.nio.file.FileSystem
import java.time.temporal.TemporalField
import java.util.jar.Manifest

/**
 * A simple [Fragment] subclass.
 */
class OraLangNumbersFragment : Fragment() {


    var isRecording  = false
    private val RECORD_REQUEST_CODE = 101
    private val STORAGE_REQUEST_CODE = 102
    var mediaRecorder: MediaRecorder? = null
    var mediaPlayer: MediaPlayer? = null
    lateinit var audioFilePath : String
    lateinit var numList : ArrayList<OraLangNums>
    lateinit var oraAdapter :OraNumAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.ora_lang_numbers, container, false)

    }

    fun initialiseMediaRecorder(){

        isRecording = true

            mediaRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
//                setOutputFile(audioFilePath)
                setOutputFile(audioFilePath)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

                try {
                    prepare()

                }catch (e: IOException){
                   Log.e("recorder", "recorder preparation failed")
                }
                start()
            }
        Toast.makeText(this.context, "Recording Started", Toast.LENGTH_LONG).show()
        }


    fun stopAudio(){
        if (isRecording){
            mediaRecorder?.stop()
            mediaRecorder?.release()
            isRecording = false
        }else{
            mediaPlayer?.release()
            mediaPlayer = null
        }
        Toast.makeText(this.context, "Recording Stoped", Toast.LENGTH_LONG).show()

    }

    private fun requestPermission(permissionType: String, requestCode: Int){

        val permission = ContextCompat.checkSelfPermission(this.requireActivity(), permissionType)

        if (permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this.requireActivity(), arrayOf(permissionType), requestCode)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when(requestCode){
            RECORD_REQUEST_CODE ->{
                if (grantResults.isEmpty()||grantResults[0]!= PackageManager.PERMISSION_GRANTED){

                    Toast.makeText(this.context, "Record permission required", Toast.LENGTH_LONG).show()
                }else{
                  requestPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_REQUEST_CODE)
                }
                return
            }

            STORAGE_REQUEST_CODE->{
                if (grantResults.isEmpty()||grantResults[0]!= PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this.context, "External Storage permission required", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }
    fun playAudio(){
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setDataSource(audioFilePath)
        mediaPlayer?.prepare()
        mediaPlayer?.start()
    }

    fun audioSetup(){
        if(!hasMicrophone()){
            Toast.makeText(this.context, "No microphone to reocrd, try selecting an Audio file instead", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this.context, "Microphone Present", Toast.LENGTH_LONG).show()
        }

//        audioFilePath = Environment.getExternalStorageDirectory().absolutePath + "/oraAudio.3gp"
        audioFilePath = context?.getExternalFilesDir(null)?.absolutePath + "/oraAudio.3gp"


        requestPermission(android.Manifest.permission.RECORD_AUDIO, RECORD_REQUEST_CODE)
    }
    fun hasMicrophone():Boolean{
        val pmanager = this.activity?.packageManager
        return pmanager!!.hasSystemFeature(PackageManager.FEATURE_MICROPHONE)
    }

    fun initializeList(){

        numList = ArrayList()

        numList.add(0, OraLangNums("One", "Okpa", R.raw.one))
        numList.add(1, OraLangNums("Two", "Evah", R.raw.two) )
        numList.add(2, OraLangNums("Three", "Eha", R.raw.three) )
        numList.add(3, OraLangNums("Four", "Enee", R.raw.four) )
        numList.add(4, OraLangNums("Five", "Iheen", R.raw.five) )
        numList.add(5, OraLangNums("Six", "Ekhan", R.raw.six) )
        numList.add(6, OraLangNums("Seven", "Ikhion", R.raw.seven) )
        numList.add(7, OraLangNums("Eight", "Een", R.raw.eight) )
        numList.add(8, OraLangNums("Nine", "Isiin", R.raw.nine) )
        numList.add(9, OraLangNums("Ten", "Igbee", R.raw.ten) )
        numList.add(10, OraLangNums("Eleven", "Ugbour", R.raw.one) )
        numList.add(11, OraLangNums("Twelve", "Igbe-vah", R.raw.two) )
        numList.add(12, OraLangNums("Thirteen", "Igbe-eha", R.raw.three) )
        numList.add(13, OraLangNums("Fourteen", "Igbe-Enee", R.raw.four) )
        numList.add(14, OraLangNums("Fifteen", "Igbe-Iheen", R.raw.five) )
        numList.add(15, OraLangNums("Sixteen", "Ke-enee-Suuee", R.raw.six) )
        numList.add(16, OraLangNums("Seventeen", "Ke-eha-Suuee", R.raw.seven) )
        numList.add(17, OraLangNums("Eighteen", "Ke-evah-Suuee", R.raw.eight) )
        numList.add(18, OraLangNums("Nineteen", "Ke-okpa-Suuee", R.raw.nine) )
        numList.add(19, OraLangNums("Twenty", "Uuee", R.raw.twenty) )
        numList.add(20, OraLangNums("Thirty", "Ogban", R.raw.thirty) )
        numList.add(21, OraLangNums("Fourty", "Egbo-evah", R.raw.fourty) )
        numList.add(22, OraLangNums("Fifty", "Egbo-evah-bi-igbe", R.raw.fifty) )
        numList.add(23, OraLangNums("Sixty", "Egbo-eha", R.raw.sixty) )
        numList.add(24, OraLangNums("Seventy", "Egbo-eha-bi-igbe", R.raw.seventy) )
        numList.add(25, OraLangNums("Eighty", "Egbo-enee", R.raw.eighty) )
        numList.add(26, OraLangNums("Ninety", "Egbo-enee-bi-igbe", R.raw.ninety) )
        numList.add(27, OraLangNums("One Hundred", "Egbo-eheen", R.raw.hundred) )


        ora_num_recycler.layoutManager = LinearLayoutManager(this.context)

        oraAdapter = OraNumAdapter(numList)

        ora_num_recycler.adapter = oraAdapter

        ora_num_recycler.addOnItemTouchListener(
            RecyclerItemClickListener(this.requireContext(), ora_num_recycler, object : RecyclerItemClickListener.OnItemClickListener {

                override fun onItemClick(view: View, position: Int) {

                    val soundPlayed = numList.get(position)

                    if (position<27){

                        if (mediaPlayer == null) {
                            mediaPlayer = MediaPlayer.create(context, soundPlayed.numIcon!!)
                            mediaPlayer?.start()
                        } else {
                            mediaPlayer?.stop()
                            mediaPlayer = MediaPlayer.create(context, soundPlayed.numIcon!!)
                            mediaPlayer?.start()
                        }

                    }else{

                        if (mediaPlayer == null) {
                            mediaPlayer = MediaPlayer.create(context, soundPlayed.recordedAudio)
                            mediaPlayer?.start()
                        } else {
                            mediaPlayer?.stop()
                            mediaPlayer = MediaPlayer.create(context, soundPlayed.recordedAudio)
                            mediaPlayer?.start()
                        }
                    }
                }

                override fun onItemLongClick(view: View?, position: Int) {
                }
            })
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initializeList()
        val animation: Animation = AnimationUtils.loadAnimation(context, R.anim.recorder_icon_blink)

//        val navController : NavController = Navigation.findNavController(view)
        fab1.setOnClickListener {

            val layoutInflater = LayoutInflater.from(this.context)
            val alertView = layoutInflater.inflate(R.layout.alert_dialog, null)
            val recordIcons = alertView.findViewById<LinearLayout>(R.id.recordIcons)
            val recordAudio = alertView.findViewById<Button>(R.id.record_audio)
            val recordImgStart = alertView.findViewById<ImageView>(R.id.start_recording)

//            val fetchAudio = alertView.findViewById<Button>(R.id.internal_audio)

            val record = alertView.findViewById<ImageView>(R.id.start_recording)
            val stop = alertView.findViewById<ImageView>(R.id.stopr_recording)
            val play = alertView.findViewById<ImageView>(R.id.play_recording)
            val delete = alertView.findViewById<ImageView>(R.id.delete_recording)


            recordAudio.setOnClickListener {
                recordIcons.visibility = View.VISIBLE
            }

            record.setOnClickListener {
                audioSetup()
                recordImgStart.setImageResource(R.drawable.ic_mic_stop_24dp)
                initialiseMediaRecorder()
                recordImgStart.startAnimation(animation)
                stop.visibility = View.VISIBLE

            }

            stop.setOnClickListener {
                stopAudio()
                recordImgStart.clearAnimation()
                recordImgStart.setImageResource(R.drawable.ic_mic_black_24dp)
                stop.visibility = View.GONE
                play.visibility = View.VISIBLE
                delete.visibility = View.VISIBLE
            }

            play.setOnClickListener {
                playAudio()
            }
            delete.setOnClickListener {
                audioFilePath = " "
                Toast.makeText(this.context, "Audio disgarded", Toast.LENGTH_LONG).show()

            }

            val alertDialog = MaterialAlertDialogBuilder(this.context)
            alertDialog.setView(alertView)
            alertDialog.setTitle(getString(R.string.alert_tilte))
            alertDialog.setCancelable(false)
            alertDialog.setPositiveButton(getString(R.string.alert_submit), null)
            alertDialog.setNegativeButton(getString(R.string.alert_cancel), null)
            val dialog = alertDialog.create()

            dialog.setOnShowListener {
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                    val engText = alertView.findViewById<EditText>(R.id.entered_eng_words)
                    val oraText = alertView.findViewById<EditText>(R.id.entered_ora_words)
                    val audioUri = Uri.parse(audioFilePath)

                    if (!engText.text.isNullOrBlank() && !oraText.text.isNullOrBlank()){

                        val  engWordEntered = engText.text.toString().trim()
                        val  oraWordEntered = oraText.text.toString().trim()

                        Toast.makeText(this.context, "strings gotten $engWordEntered $oraWordEntered", Toast.LENGTH_LONG).show()

                        oraAdapter.addOraNumber(OraLangNums(engWordEntered, oraWordEntered, null, audioUri ))

                        Toast.makeText(this.context, "New details saved", Toast.LENGTH_LONG).show()
                        dialog.dismiss()

                    }else{
                        Toast.makeText(this.context, "Ensure you have entered the English and Ora Words", Toast.LENGTH_LONG).show()
                    }
            }
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener {
                    dialog.dismiss()
                }
            }
            dialog.show()
        }
    }
}


