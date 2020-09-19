package com.flowz.introtooralanguage.display

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.flowz.introtooralanguage.R
import kotlinx.android.synthetic.main.activity_audio_test.*
import java.io.File
import java.io.FileDescriptor
import java.io.FileInputStream
import java.io.IOException

class AudioTestActivity : AppCompatActivity() {

    lateinit var audioPicked : Uri
    var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_test)


        select_audio.setOnClickListener {
            val audioIntent = Intent()
            audioIntent.setType("audio/*")
            audioIntent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(audioIntent, 10)
        }

        play_test_audio.setOnClickListener {

//            mediaPlayer?.setDataSource(context, audioPicked)
//            mediaPlayer?.prepare()
//            mediaPlayer?.start()
//            Toast.makeText(this, "Music file" + audioPicked, Toast.LENGTH_LONG).show()
            playContentUri(audioPicked)

        }

    }

    fun playContentUri(uri: Uri) {
//        val mMediaPlayer = MediaPlayer().apply {

        if (mediaPlayer!=null) {

            mediaPlayer?.stop()

            try {
                mediaPlayer = MediaPlayer().apply {
                    setDataSource(application, uri)
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
        }else{

            try {
                mediaPlayer = MediaPlayer().apply {
                    setDataSource(application, uri)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


        if (!(resultCode == Activity.RESULT_OK || data != null || data?.data != null)){
            Toast.makeText(this, "Error in getting music file", Toast.LENGTH_LONG).show()
        }

        if (requestCode == 10 && resultCode == Activity.RESULT_OK){

            if (data != null) {

                val muri = data.data

                showPath.text = muri.toString()

                audioPicked = muri!!

                super.onActivityResult(requestCode, resultCode, data)

            }

        }
    }

}


