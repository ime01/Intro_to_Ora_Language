package com.flowz.introtooralanguage.extensions

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.fragment.app.Fragment
import com.flowz.introtooralanguage.R
import com.flowz.introtooralanguage.data.OraLangNums
import com.flowz.introtooralanguage.display.numbers.OraLangNumbersFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
//import com.localazy.android.Localazy.getString
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException

/**
 * @author robin
 * Created on 10/11/20
 */

var mediaPlayer: MediaPlayer? = null


fun AppCompatActivity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun TextInputEditText.takeWords() : String{
    return this.text.toString().trim()
}
fun EditText.takeWords() : String{
    return this.text.toString().trim()
}

fun playAnimation(context:Context, int: Int, view:View){

    val animation = AnimationUtils.loadAnimation(context, int)
    view.startAnimation(animation)
}

fun showToast(string: String, context: Context){

    Toast.makeText(context, string, Toast.LENGTH_LONG).show()
}

fun showSnackbar(view: View, string: String){

    Snackbar.make(view, string, Snackbar.LENGTH_LONG).show()
}

fun playContentUri(uri: Uri, context: Context) {
//        val mMediaPlayer = MediaPlayer().apply {

    if (mediaPlayer != null) {

        mediaPlayer?.stop()

        try {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(context!!, uri)
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                prepare()
            }
            mediaPlayer?.start()
        } catch (e: IOException) {

            mediaPlayer = null
            mediaPlayer?.release()
        }
    } else {

        try {
            mediaPlayer = MediaPlayer().apply {

                setDataSource(context!!, uri)

                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                prepare()
            }
            mediaPlayer?.start()
        } catch (e: IOException) {

            mediaPlayer = null
            mediaPlayer?.release()
        }

    }
}

fun playContentInt(int: Int, context: Context) {

        if (mediaPlayer == null) {
                                mediaPlayer = MediaPlayer.create(context, int)
                                mediaPlayer?.start()

                            } else {
                                mediaPlayer?.stop()
                                mediaPlayer = MediaPlayer.create(context, int)
                                mediaPlayer?.start()
                            }
    }


//fun editOraWord(context: Context){
//
//    var recordButtonClicked: Boolean = false
//    val AUDIO_REQUEST_CODE = 1
//    val KEY_COUNT_VALUE = "key_count"
//    var audioFilePath: String
//
//
//    val layoutInflater = LayoutInflater.from(context)
//    val alertView = layoutInflater.inflate(R.layout.alert_dialog, null)
//    val recordIcons = alertView.findViewById<LinearLayout>(R.id.recordIcons)
//    val recordAudio = alertView.findViewById<Button>(R.id.record_audio)
//    val recordImgStart = alertView.findViewById<ImageView>(R.id.start_recording)
//    val fetchAudio = alertView.findViewById<Button>(R.id.internal_audio)
//
//    val record = alertView.findViewById<ImageView>(R.id.start_recording)
//    val stop = alertView.findViewById<ImageView>(R.id.stopr_recording)
//    val play = alertView.findViewById<ImageView>(R.id.play_recording)
//    val delete = alertView.findViewById<ImageView>(R.id.delete_recording)
//
//
////    recordAudio.setOnClickListener {
////        recordIcons.visibility = View.VISIBLE
////        recordButtonClicked = true
////    }
////
////    fetchAudio.setOnClickListener {
////        recordButtonClicked = false
////        val audioIntent = Intent()
////        audioIntent.setType("audio/*")
////        val action = audioIntent.setAction(Intent.ACTION_GET_CONTENT)
////        startActivityForResult(audioIntent, action)
////    }
////
////    record.setOnClickListener {
////        audioSetup()
////        recordImgStart.setImageResource(R.drawable.ic_mic_stop_24dp)
////        initialiseMediaRecorder()
////        recordImgStart.startAnimation(animation)
////        stop.visibility = View.VISIBLE
////    }
////
////    stop.setOnClickListener {
////        stopAudio()
////        recordImgStart.clearAnimation()
////        recordImgStart.setImageResource(R.drawable.ic_mic_black_24dp)
////        stop.visibility = View.GONE
////        play.visibility = View.VISIBLE
////        delete.visibility = View.VISIBLE
////    }
////
////    play.setOnClickListener {
////        playAudio()
////    }
////    delete.setOnClickListener {
////        audioFilePath = " "
////        showToast("Audio disgarded", context)
////
////    }
//
//    val alertDialog = MaterialAlertDialogBuilder(context)
//    alertDialog.setView(alertView)
//    alertDialog.setTitle(getString(context,R.string.alert_tilte))
//    alertDialog.setCancelable(false)
//    alertDialog.setPositiveButton(getString(context, R.string.alert_submit), null)
//    alertDialog.setNegativeButton(getString(context,R.string.alert_cancel), null)
//    val dialog = alertDialog.create()
//
//    dialog.setOnShowListener {
//        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
//            val engText = alertView.findViewById<EditText>(R.id.entered_eng_words)
//            val oraText = alertView.findViewById<EditText>(R.id.entered_ora_words)
//            val audioUri = Uri.parse(audioFilePath)
//
//            if (recordButtonClicked == false) {
//
//                showToast("Select Audio Button clicked",context)
//
//                val chosenAudio = Uri.parse(audioFilePath)
//
//                if (!engText.text.isNullOrBlank() && !oraText.text.isNullOrBlank()) {
//
//                    val engWordEntered = engText.text.toString().trim()
//                    val oraWordEntered = oraText.text.toString().trim()
//
//                    showToast("strings gotten $engWordEntered $oraWordEntered",  context)
//
//
////                            SaveOraElement(engWordEntered, oraWordEntered, null, chosenAudio)
//
//                    GlobalScope.launch {
//                        oraAdapter.addOraNumber(OraLangNums(engWordEntered, oraWordEntered, null, audioUri))
//
//                    }
//
//
//                    showToast("New details saved", context)
//                    dialog.dismiss()
//
//                } else {
//                    showToast("Ensure you have entered the English and Ora Words", context)
//                }
//
//
//            } else if (recordButtonClicked == true) {
//
//                if (!engText.text.isNullOrBlank() && !oraText.text.isNullOrBlank()) {
//
//                    val engWordEntered = engText.text.toString().trim()
//                    val oraWordEntered = oraText.text.toString().trim()
//
//                    showToast("strings gotten $engWordEntered $oraWordEntered", context)
//
//                    SaveOraElement(engWordEntered, oraWordEntered, null, audioUri)
//
//                    showToast("New details saved", context)
//
//                    dialog.dismiss()
//
//                } else {
//                    showToast("Ensure you have entered the English and Ora Words", context)
//                }
//            }
//        }
//        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener {
//            dialog.dismiss()
//        }
//    }
//    dialog.show()
//}