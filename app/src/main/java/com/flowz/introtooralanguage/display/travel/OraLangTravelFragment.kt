package com.flowz.introtooralanguage.display.travel


import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager

import com.flowz.introtooralanguage.R
import com.flowz.introtooralanguage.adapters.HouseWordsAdapter
import com.flowz.introtooralanguage.adapters.OutdoorWordsAdapter
import com.flowz.introtooralanguage.adapters.TravelWordsAdapter
import com.flowz.introtooralanguage.data.models.TravelWordsModel
import com.flowz.introtooralanguage.display.numbers.NumbersRepository
import com.flowz.introtooralanguage.data.room.OraWordsDatabase
import com.flowz.introtooralanguage.display.numbers.OraLangNumbersFragment
import com.flowz.introtooralanguage.extensions.playContentUri
import com.flowz.introtooralanguage.extensions.showToast
import com.flowz.introtooralanguage.workmanager.ReminderWorker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_ora_lang_travel.*
import kotlinx.android.synthetic.main.fragment_ora_lang_travel.fab2
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */

@AndroidEntryPoint
class OraLangTravelFragment : Fragment(), TravelWordsAdapter.RowClickListener {

    private val travelWordViewModel by viewModels<TravelWordViewModel>()

    var isRecording = false
    private val RECORD_REQUEST_CODE = 101
    private val STORAGE_REQUEST_CODE = 102
    var mediaRecorder: MediaRecorder? = null
    var mediaPlayer: MediaPlayer? = null
    lateinit var audioFilePath: String
    lateinit var numList: ArrayList<TravelWordsModel>
    var searchViewList: ArrayList<TravelWordsModel> = ArrayList()
    lateinit var uri: Uri
    lateinit var selectedPath: Uri
    var recordButtonClicked: Boolean = false
    val addOraWordTag = "addOraWordTag"


    companion object {
        const val AUDIO_REQUEST_CODE = 1
        const val KEY_COUNT_VALUE = "key_count"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ora_lang_travel, container, false)
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
        showToast("Recording Started", requireContext())
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
        showToast("Recording Stoped",requireContext() )
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

                    showToast("Record permission required", requireContext())
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
                    showToast("External Storage permission required", requireContext())
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
            showToast("No microphone to reocrd, try selecting an Audio file instead", requireContext())
        } else {
            showToast("Microphone Present", requireContext())
        }
//        audioFilePath = Environment.getExternalStorageDirectory().absolutePath + "/oraAudio.3gp"
        audioFilePath = context?.getExternalFilesDir(null)?.absolutePath + "/oraAudio.3gp"

        requestPermission(android.Manifest.permission.RECORD_AUDIO, RECORD_REQUEST_CODE)
    }

    fun hasMicrophone(): Boolean {
        val pmanager = this.activity?.packageManager
        return pmanager!!.hasSystemFeature(PackageManager.FEATURE_MICROPHONE)
    }

    fun playContentUri(uri: Uri) {

        if (mediaPlayer != null) {

            mediaPlayer?.stop()

            try {
                mediaPlayer = MediaPlayer().apply {
                    setDataSource(requireContext(), uri)
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

                    setDataSource(requireContext(), uri)

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

        if (!(resultCode == Activity.RESULT_OK || data != null || data?.data != null)) {
            showToast("Error in getting music file", requireContext())
        }

        if (requestCode == OraLangNumbersFragment.AUDIO_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            if (data != null) {

                val muri = data.data

                if (data != null) {

                    val muri = data.data

                    audioFilePath = muri.toString()

                    playContentUri(muri!!)

                    super.onActivityResult(requestCode, resultCode, data)

                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        var mediaPlayer: MediaPlayer? = null
        val travelList : ArrayList<TravelWordsModel> = ArrayList()

        travelList.add(0, TravelWordsModel("We'll stop for a break here", "Mi ma muze fietian waun",  Uri.parse("android.resource://" + context?.packageName + "/raw/one")) )
        travelList.add(1, TravelWordsModel(" Shortly we'll be back on our journey", "Mi ma gbe gbe bee vbi o shaan", Uri.parse("android.resource://" + context?.packageName + "/raw/one")) )
        travelList.add(2, TravelWordsModel("The light says stop ", "Uru okpa owee nu muze",  Uri.parse("android.resource://" + context?.packageName + "/raw/one")) )
        travelList.add(3, TravelWordsModel("Here are my papers ", "Ka ough kpebe men",  Uri.parse("android.resource://" + context?.packageName + "/raw/one")) )
        travelList.add(4, TravelWordsModel("You can go", "Kha Shaan", Uri.parse("android.resource://" + context?.packageName + "/raw/one")) )
        travelList.add(5, TravelWordsModel("We've on the road for some time", "Or khuiee nii mai da rii vbi ukpodee",  Uri.parse("android.resource://" + context?.packageName + "/raw/one")) )
        travelList.add(6, TravelWordsModel("Sit in the front seat", "Dey gha vbi odaoo",  Uri.parse("android.resource://" + context?.packageName + "/raw/one")) )
        travelList.add(7, TravelWordsModel("Sit in the back seat", "Dey gha vbi Ehimin",  Uri.parse("android.resource://" + context?.packageName + "/raw/one")) )
        travelList.add(8, TravelWordsModel("Stop here", "Muze ma ann", Uri.parse("android.resource://" + context?.packageName + "/raw/one")) )
        travelList.add(9, TravelWordsModel("We will get down here", "Mi ma do otoi maan",  Uri.parse("android.resource://" + context?.packageName + "/raw/one")) )
        travelList.add(10, TravelWordsModel("Thanks for taking me", "Uzor kah nu dah mu mee",  Uri.parse("android.resource://" + context?.packageName + "/raw/one")) )
        searchViewList.addAll(travelList)

        travelWordViewModel.insertListOfTravelWords(travelList)

        ora_travel_recycler.layoutManager = LinearLayoutManager(this.context)

        val oraAdapter = TravelWordsAdapter(this)


        val animation: Animation = AnimationUtils.loadAnimation(context, R.anim.recorder_icon_blink)

        travelWordViewModel.travelWordsFromDb.observe(viewLifecycleOwner, Observer {

            oraAdapter.submitList(it)
            ora_travel_recycler.adapter = oraAdapter

        })




        fab2.setOnClickListener {

            val layoutInflater = LayoutInflater.from(this.context)
            val alertView = layoutInflater.inflate(R.layout.alert_dialog, null)
            val recordIcons = alertView.findViewById<LinearLayout>(R.id.recordIcons)
            val recordAudio = alertView.findViewById<Button>(R.id.record_audio)
            val recordImgStart = alertView.findViewById<ImageView>(R.id.start_recording)
            val fetchAudio = alertView.findViewById<Button>(R.id.internal_audio)

            val record = alertView.findViewById<ImageView>(R.id.start_recording)
            val stop = alertView.findViewById<ImageView>(R.id.stopr_recording)
            val play = alertView.findViewById<ImageView>(R.id.play_recording)
            val delete = alertView.findViewById<ImageView>(R.id.delete_recording)


            recordAudio.setOnClickListener {
                recordIcons.visibility = View.VISIBLE
                recordButtonClicked = true
            }

            fetchAudio.setOnClickListener {
                recordButtonClicked = false
                val audioIntent = Intent()
                audioIntent.setType("audio/*")
                audioIntent.setAction(Intent.ACTION_GET_CONTENT)
                startActivityForResult(audioIntent,
                    OraLangNumbersFragment.AUDIO_REQUEST_CODE
                )
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
                showToast("Audio disgarded", requireContext())

            }

            val alertDialog = MaterialAlertDialogBuilder(requireContext())
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

                    if (recordButtonClicked == false) {

                        showToast("Select Audio Button clicked",requireContext())

                        val chosenAudio = Uri.parse(audioFilePath)

                        if (!engText.text.isNullOrBlank() && !oraText.text.isNullOrBlank()) {

                            val engWordEntered = engText.text.toString().trim()
                            val oraWordEntered = oraText.text.toString().trim()

                            showToast("strings gotten $engWordEntered $oraWordEntered",  requireContext())


                            SaveOraElement(engWordEntered, oraWordEntered,  chosenAudio)

                            showToast("New details saved", requireContext())
                            dialog.dismiss()

                        } else {
                            showToast("Ensure you have entered the English and Ora Words", requireContext())
                        }


                    } else if (recordButtonClicked == true) {

                        if (!engText.text.isNullOrBlank() && !oraText.text.isNullOrBlank()) {

                            val engWordEntered = engText.text.toString().trim()
                            val oraWordEntered = oraText.text.toString().trim()

                            showToast("strings gotten $engWordEntered $oraWordEntered", requireContext())

                            SaveOraElement(engWordEntered, oraWordEntered,  audioUri)

                            showToast("New details saved", requireContext())

                            dialog.dismiss()

                        } else {
                            showToast("Ensure you have entered the English and Ora Words", requireContext())
                        }
                    }
                }
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener {
                    dialog.dismiss()
                }
            }
            dialog.show()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.menu_layout, menu)
        val menuItem = menu!!.findItem(R.id.search_oraword)


        if(menuItem != null){

            val searchView = menuItem.actionView as androidx.appcompat.widget.SearchView

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {

                    if (newText!!.isNotEmpty()){

                        searchViewList.clear()

                        val search = newText.toLowerCase(Locale.getDefault())

                        numList.forEach {

                            if (it.engNum.toLowerCase(Locale.getDefault()).contains(search)){

                                searchViewList.add(it)

                            }
                            ora_travel_recycler.adapter!!.notifyDataSetChanged()
                        }
                    } else{

                        searchViewList.clear()
                        searchViewList.addAll(numList)
                        ora_travel_recycler.adapter!!.notifyDataSetChanged()
                    }

                    return true
                }

            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){

            R.id.remind_me ->{
                showToast("Reminder Clicked",this.requireContext() )
                selectDateandTimeForRemeinder()

                true
            }

            R.id.search_oraword -> {
                showToast("Search clicked", this.requireContext())
                true
            }
            else -> super.onOptionsItemSelected(item)

        }
    }

    fun selectDateandTimeForRemeinder(){

        val layoutInflater = LayoutInflater.from(this.context)
        val setTimeDialogView = layoutInflater.inflate(R.layout.date_n_time_picker_alert_dialog, null)
        val enteredDate = setTimeDialogView.findViewById<DatePicker>(R.id.date_p)
        val enteredTime = setTimeDialogView.findViewById<ru.ifr0z.timepickercompact.TimePickerCompact>(R.id.time_p)


        val alertDialog = MaterialAlertDialogBuilder(requireContext())
        alertDialog.setView(setTimeDialogView)
        alertDialog.setTitle(getString(R.string.select_time_dialog_title))
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton(getString(R.string.date_picker_alert_positive), null)
        alertDialog.setNegativeButton(getString(R.string.date_picker_alert_negative), null)
        val dialog = alertDialog.create()

        dialog.setOnShowListener {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {


                setOneTimeWorkRequest(enteredDate, enteredTime)


                dialog.dismiss()

            }
            dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener {
                dialog.dismiss()
            }
        }
        dialog.show()
    }



    fun setOneTimeWorkRequest(date_p : DatePicker, time_p : ru.ifr0z.timepickercompact.TimePickerCompact ) {

        val customCalendar = java.util.Calendar.getInstance()

        customCalendar.set(
            date_p.year, date_p.month, date_p.dayOfMonth, time_p.hour, time_p.minute, 0
        )


        val customTime = customCalendar.timeInMillis
        val currentTime = System.currentTimeMillis()

        val delay = customTime - currentTime


        val workManager = WorkManager.getInstance(requireContext())


        val data : Data = Data.Builder()
            .putInt(OraLangNumbersFragment.KEY_COUNT_VALUE, 125)
            .build()

        val constants = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val notificationRequest = OneTimeWorkRequest.Builder(ReminderWorker::class.java)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setConstraints(constants)
            .setInputData(data)
            .addTag(addOraWordTag)
            .build()


        workManager.enqueue(notificationRequest)

//        workManager.beginUniqueWork(addOraWordTag, ExistingWorkPolicy.REPLACE, notificationRequest)

        workManager.getWorkInfoByIdLiveData(notificationRequest.id).observe(viewLifecycleOwner, Observer {

            val workSetStatus = it.state.name

            showToast("Status of workManager task is : $workSetStatus", requireContext())

        })

    }

    fun SaveOraElement(engWord: String, oraWord: String,  enteredAudio: Uri) =
        travelWordViewModel.insertTravelWord(TravelWordsModel(engWord, oraWord, enteredAudio))


    override fun onPlayOraWordClickListener(travelWord: TravelWordsModel) {
        travelWord.recordedAudio?.let {playContentUri(it, requireContext())
        }
    }

    override fun onEditOraWordClickListener(travelWord: TravelWordsModel) {
        val action = OraLangTravelFragmentDirections.actionOraLangTravelFragmentToEditOraWordFragment()
//                                action.oraLangNums = oraLangNumList[position]
        action.arguments.putInt("type", 3)
        action.travelWord = travelWord
        Navigation.findNavController(requireView()).navigate(action)

    }

    override fun onDeleteOraWordClickListener(travelWord: TravelWordsModel) {
        travelWordViewModel.deleteTravelWord(travelWord)
    }

    override fun onItemClickListener(travelWord: TravelWordsModel) {

    }


}
