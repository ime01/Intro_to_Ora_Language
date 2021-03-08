package com.flowz.introtooralanguage.display.outdoor


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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager

import com.flowz.introtooralanguage.R
import com.flowz.introtooralanguage.adapters.OraWordsAdapter
//import com.flowz.introtooralanguage.adapters.OraNumbersAdapter
//import com.flowz.introtooralanguage.adapters.OraWordsAdapter
import com.flowz.introtooralanguage.data.OraLangNums
import com.flowz.introtooralanguage.data.room.OraNumRepository
import com.flowz.introtooralanguage.data.room.OraWordsDatabase
import com.flowz.introtooralanguage.display.base.ScopedFragment
import com.flowz.introtooralanguage.display.numbers.OraLangNumbersFragment
import com.flowz.introtooralanguage.display.numbers.OraNumberViewModel1
import com.flowz.introtooralanguage.display.numbers.OraNumberViewModelFactory1
import com.flowz.introtooralanguage.extensions.playContentUri
//import com.flowz.introtooralanguage.display.numbers.OraNumberViewModel
//import com.flowz.introtooralanguage.display.numbers.OraNumberViewModelFactory
import com.flowz.introtooralanguage.extensions.showToast
import com.flowz.introtooralanguage.recyclerviewlistener.RecyclerItemClickListener
import com.flowz.introtooralanguage.workmanager.ReminderWorker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_ora_lang_house.*
import kotlinx.android.synthetic.main.fragment_ora_lang_outdoor.*
import kotlinx.android.synthetic.main.ora_lang_numbers.*
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class OraLangOutdoorFragment : ScopedFragment(), OraWordsAdapter.RowClickListener {

    var isRecording = false
    private val RECORD_REQUEST_CODE = 101
    private val STORAGE_REQUEST_CODE = 102
    var mediaRecorder: MediaRecorder? = null
    var mediaPlayer: MediaPlayer? = null
    lateinit var audioFilePath: String
    lateinit var numList: ArrayList<OraLangNums>
    var searchViewList: ArrayList<OraLangNums> = ArrayList()
    lateinit var uri: Uri
    lateinit var selectedPath: Uri
    var recordButtonClicked: Boolean = false
    private lateinit var oraWordsViewModel: OraNumberViewModel1
    val addOraWordTag = "addOraWordTag"


    companion object {
        const val AUDIO_REQUEST_CODE = 1
        const val KEY_COUNT_VALUE = "key_count"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ora_lang_outdoor, container, false)
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
        showToast("Recording Stoped", this.context!!)
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
            showToast(
                "No microphone to reocrd, try selecting an Audio file instead",
                this.context!!
            )
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

    fun playContentUri(uri: Uri) {
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

                    playContentUri(muri!!)

                    super.onActivityResult(requestCode, resultCode, data)

                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var mediaPlayer: MediaPlayer? = null
        val OutdoorList: ArrayList<OraLangNums> = ArrayList()
//
        OutdoorList.add(
            0,
            OraLangNums(
                "I am going to the market",
                "Ilo deh vbee kin",
                null,
                Uri.parse("android.resource://" + context?.packageName + "/raw/one")
            )
        )
        OutdoorList.add(
            1,
            OraLangNums(
                "How much is this item",
                "Ekah ighor na",
                null,
                Uri.parse("android.resource://" + context?.packageName + "/raw/one")
            )
        )
        OutdoorList.add(
            2,
            OraLangNums(
                "I will pay for this",
                "Mio hoosa ghor ona",
                null,
                Uri.parse("android.resource://" + context?.packageName + "/raw/one")
            )
        )
        OutdoorList.add(
            3,
            OraLangNums(
                "I am going out",
                "Ilo deh vbooee",
                null,
                Uri.parse("android.resource://" + context?.packageName + "/raw/one")
            )
        )
        OutdoorList.add(
            4,
            OraLangNums(
                "walk fast",
                "Tuah shaan",
                null,
                Uri.parse("android.resource://" + context?.packageName + "/raw/one")
            )
        )
        OutdoorList.add(
            5,
            OraLangNums(
                "put the item down",
                "Meo onee mi eeh wo otoi",
                null,
                Uri.parse("android.resource://" + context?.packageName + "/raw/one")
            )
        )
        OutdoorList.add(
            6,
            OraLangNums(
                "Put on your shoes",
                "Whei ebata weh",
                null,
                Uri.parse("android.resource://" + context?.packageName + "/raw/one")
            )
        )
        OutdoorList.add(
            7,
            OraLangNums(
                "You can play here",
                "Khi een maan",
                null,
                Uri.parse("android.resource://" + context?.packageName + "/raw/one")
            )
        )
        OutdoorList.add(
            8,
            OraLangNums(
                "I'll wait for you here",
                " Meo muze khe maan",
                null,
                Uri.parse("android.resource://" + context?.packageName + "/raw/one")
            )
        )
        OutdoorList.add(
            9,
            OraLangNums(
                "How are you",
                "Or nhe gbe",
                null,
                Uri.parse("android.resource://" + context?.packageName + "/raw/one")
            )
        )
        OutdoorList.add(
            10,
            OraLangNums(
                "I am fine",
                "Orfure",
                null,
                Uri.parse("android.resource://" + context?.packageName + "/raw/one")
            )
        )

        searchViewList.addAll(OutdoorList)


        val application = requireNotNull(activity).application
        val dao = OraWordsDatabase.invoke(application)
        val repository = OraNumRepository(dao.oraWordsDao())
        val viewModelFactory = OraNumberViewModelFactory1(repository)

        oraWordsViewModel = ViewModelProviders.of(this, viewModelFactory).get(OraNumberViewModel1::class.java)

        val oraAdapter = OraWordsAdapter(this)
        ora_outdoor_recycler.layoutManager = LinearLayoutManager(this.context)

        val gottenWords = oraWordsViewModel.oraWords.observe(this, Observer {

            it.let {

                val finalList = searchViewList + it

                oraAdapter.submitList(finalList)
                ora_outdoor_recycler.adapter = oraAdapter

            }
        })


        val animation: Animation = AnimationUtils.loadAnimation(context, R.anim.recorder_icon_blink)

        fab4.setOnClickListener {

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
                startActivityForResult(
                    audioIntent,
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
                showToast("Audio disgarded", this.context!!)

            }

            val alertDialog = MaterialAlertDialogBuilder(this.context!!)
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

                        showToast("Select Audio Button clicked", this.context!!)

                        val chosenAudio = Uri.parse(audioFilePath)

                        if (!engText.text.isNullOrBlank() && !oraText.text.isNullOrBlank()) {

                            val engWordEntered = engText.text.toString().trim()
                            val oraWordEntered = oraText.text.toString().trim()

                            showToast(
                                "strings gotten $engWordEntered $oraWordEntered",
                                this.context!!
                            )


                            SaveOraElement(engWordEntered, oraWordEntered, null, chosenAudio)

                            showToast("New details saved", this.context!!)
                            dialog.dismiss()

                        } else {
                            showToast(
                                "Ensure you have entered the English and Ora Words",
                                this.context!!
                            )
                        }


                    } else if (recordButtonClicked == true) {

                        if (!engText.text.isNullOrBlank() && !oraText.text.isNullOrBlank()) {

                            val engWordEntered = engText.text.toString().trim()
                            val oraWordEntered = oraText.text.toString().trim()

                            showToast(
                                "strings gotten $engWordEntered $oraWordEntered",
                                this.context!!
                            )

                            SaveOraElement(engWordEntered, oraWordEntered, null, audioUri)

                            showToast("New details saved", this.context!!)

                            dialog.dismiss()

                        } else {
                            showToast(
                                "Ensure you have entered the English and Ora Words",
                                this.context!!
                            )
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


        if (menuItem != null) {

            val searchView = menuItem.actionView as androidx.appcompat.widget.SearchView

//            val editText = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
//            editText.hint = "Search..."

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {

                    if (newText!!.isNotEmpty()) {

                        searchViewList.clear()

                        val search = newText.toLowerCase(Locale.getDefault())

                        numList.forEach {

                            if (it.engNum.toLowerCase(Locale.getDefault()).contains(search)) {

                                searchViewList.add(it)

                            }
                            ora_outdoor_recycler.adapter!!.notifyDataSetChanged()
                        }
                    } else {

                        searchViewList.clear()
                        searchViewList.addAll(numList)
                        ora_outdoor_recycler.adapter!!.notifyDataSetChanged()
                    }

                    return true
                }

            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.remind_me -> {
                showToast("Reminder Clicked", this.requireContext())
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

    fun selectDateandTimeForRemeinder() {

        val layoutInflater = LayoutInflater.from(this.context)
        val setTimeDialogView =
            layoutInflater.inflate(R.layout.date_n_time_picker_alert_dialog, null)
        val enteredDate = setTimeDialogView.findViewById<DatePicker>(R.id.date_p)
        val enteredTime =
            setTimeDialogView.findViewById<ru.ifr0z.timepickercompact.TimePickerCompact>(R.id.time_p)


        val alertDialog = MaterialAlertDialogBuilder(this.context!!)
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


    fun setOneTimeWorkRequest(
        date_p: DatePicker,
        time_p: ru.ifr0z.timepickercompact.TimePickerCompact
    ) {

        val customCalendar = java.util.Calendar.getInstance()

        customCalendar.set(
            date_p.year, date_p.month, date_p.dayOfMonth, time_p.hour, time_p.minute, 0
        )


        val customTime = customCalendar.timeInMillis
        val currentTime = System.currentTimeMillis()

        val delay = customTime - currentTime


        val workManager = WorkManager.getInstance(this.context!!)


        val data: Data = Data.Builder()
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

        workManager.getWorkInfoByIdLiveData(notificationRequest.id)
            .observe(viewLifecycleOwner, Observer {

                val workSetStatus = it.state.name

                showToast("Status of workManager task is : $workSetStatus", this.context!!)

            })

    }

    fun SaveOraElement(engWord: String, oraWord: String, numIcon: Int?, enteredAudio: Uri) =
        launch {
            oraWordsViewModel.saveOraElement(engWord, oraWord, numIcon, enteredAudio)
        }

    override fun onPlayOraWordClickListener(oraWords: OraLangNums) {
        oraWords.recordedAudio?.let { playContentUri(it, context!!) }
    }

    override fun onEditOraWordClickListener(oraWords: OraLangNums) {
        val action =
            OraLangOutdoorFragmentDirections.actionOraLangOutdoorFragmentToEditOraWordFragment()
//                                action.oraLangNums = oraLangNumList[position]
        action.oraLangNums = oraWords
        Navigation.findNavController(view!!).navigate(action)

}

    override fun onDeleteOraWordClickListener(oraWords: OraLangNums) {
        launch {

            oraWordsViewModel.deleteOraElement(oraWords)
        }

    }

    override fun onItemClickListener(oraWords: OraLangNums) {

    }

}
