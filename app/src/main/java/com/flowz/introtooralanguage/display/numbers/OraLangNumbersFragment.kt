package com.flowz.introtooralanguage.display.numbers


import android.app.Activity
import android.content.Context
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
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.*

import com.flowz.introtooralanguage.R
//import com.flowz.introtooralanguage.adapters.OraNumbersAdapter
import com.flowz.introtooralanguage.adapters.OraNumbersAdapter1
import com.flowz.introtooralanguage.data.OraLangNums
import com.flowz.introtooralanguage.data.room.OraNumRepository
import com.flowz.introtooralanguage.data.room.OraWordsDao
import com.flowz.introtooralanguage.data.room.OraWordsDatabase
import com.flowz.introtooralanguage.display.base.ScopedFragment
import com.flowz.introtooralanguage.extensions.playContentInt
import com.flowz.introtooralanguage.extensions.playContentUri
import com.flowz.introtooralanguage.extensions.showSnackbar
import com.flowz.introtooralanguage.extensions.showToast
import com.flowz.introtooralanguage.recyclerviewlistener.RecyclerItemClickListener
import com.flowz.introtooralanguage.workmanager.ReminderWorker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.ora_lang_numbers.*
import kotlinx.coroutines.*
import java.io.IOException
import java.lang.System.currentTimeMillis
import java.util.*
import java.util.concurrent.TimeUnit.MILLISECONDS
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class OraLangNumbersFragment : ScopedFragment(), OraNumbersAdapter1.RowClickListener {

    var isRecording = false
    private val RECORD_REQUEST_CODE = 101
    private val STORAGE_REQUEST_CODE = 102
    var mediaRecorder: MediaRecorder? = null
    var mediaPlayer: MediaPlayer? = null
    lateinit var audioFilePath: String
    lateinit var numList: ArrayList<OraLangNums>
    var searchViewList: ArrayList<OraLangNums> = ArrayList()
//    lateinit var oraAdapter: OraNumbersAdapter
    lateinit var oraAdapter: OraNumbersAdapter1
    lateinit var uri: Uri
    lateinit var selectedPath: Uri
    var recordButtonClicked: Boolean = false
    private lateinit var numberViewModel: OraNumberViewModel1
    val addOraWordTag = "addOraWordTag"


    companion object {
        const val AUDIO_REQUEST_CODE = 1
        const val KEY_COUNT_VALUE = "key_count"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.ora_lang_numbers, container, false)

        return view

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

        if (requestCode == AUDIO_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

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


     fun initializeList() {


        numList = ArrayList()

        numList.add(0, OraLangNums("One", "Okpa", null, Uri.parse(R.raw.one.toString())))
        numList.add(1, OraLangNums("Two", "Evah", null, Uri.parse(R.raw.one.toString())))
        numList.add(2, OraLangNums("Three", "Eha", null, Uri.parse(R.raw.one.toString())))
        numList.add(3, OraLangNums("Four", "Enee", R.raw.four))
        numList.add(4, OraLangNums("Five", "Iheen", R.raw.five))
        numList.add(5, OraLangNums("Six", "Ekhan", R.raw.six))
        numList.add(6, OraLangNums("Seven", "Ikhion", R.raw.seven))
        numList.add(7, OraLangNums("Eight", "Een", R.raw.eight))
        numList.add(8, OraLangNums("Nine", "Isiin", R.raw.nine))
        numList.add(9, OraLangNums("Ten", "Igbee", R.raw.ten))
        numList.add(10, OraLangNums("Eleven", "Ugbour", R.raw.one))
        numList.add(11, OraLangNums("Twelve", "Igbe-vah", R.raw.two))
        numList.add(12, OraLangNums("Thirteen", "Igbe-eha", R.raw.three))
        numList.add(13, OraLangNums("Fourteen", "Igbe-Enee", R.raw.four))
        numList.add(14, OraLangNums("Fifteen", "Igbe-Iheen", R.raw.five))
        numList.add(15, OraLangNums("Sixteen", "Ke-enee-Suuee", R.raw.six))
        numList.add(16, OraLangNums("Seventeen", "Ke-eha-Suuee", R.raw.seven))
        numList.add(17, OraLangNums("Eighteen", "Ke-evah-Suuee", R.raw.eight))
        numList.add(18, OraLangNums("Nineteen", "Ke-okpa-Suuee", R.raw.nine))
        numList.add(19, OraLangNums("Twenty", "Uuee", R.raw.twenty))
        numList.add(20, OraLangNums("Thirty", "Ogban", R.raw.thirty))
        numList.add(21, OraLangNums("Fourty", "Egbo-evah", R.raw.fourty))
        numList.add(22, OraLangNums("Fifty", "Egbo-evah-bi-igbe", R.raw.fifty))
        numList.add(23, OraLangNums("Sixty", "Egbo-eha", R.raw.sixty))
        numList.add(24, OraLangNums("Seventy", "Egbo-eha-bi-igbe", R.raw.seventy))
        numList.add(25, OraLangNums("Eighty", "Egbo-enee", R.raw.eighty))
        numList.add(26, OraLangNums("Ninety", "Egbo-enee-bi-igbe", R.raw.ninety))
        numList.add(27, OraLangNums("One Hundred", "Egbo-eheen", R.raw.hundred))

        searchViewList.addAll(numList)

        ora_num_recycler.layoutManager = LinearLayoutManager(this.context)

//        oraAdapter = OraNumbersAdapter(this.requireContext(), OraWordsDatabase.invoke(this.context!!),   searchViewList)
        oraAdapter = OraNumbersAdapter1(this)

//        val alphaAdapter = AlphaInAnimationAdapter(oraAdapter)
//        ora_num_recycler.adapter = ScaleInAnimationAdapter(alphaAdapter)


//        ora_num_recycler.itemAnimator = null


        val gottenWords = numberViewModel.oraWords.observe(this, Observer {

            it.let {

                val finalList = searchViewList + it

                oraAdapter.submitList(finalList)
                ora_num_recycler.adapter = oraAdapter

            }
        })



//        ora_num_recycler.itemAnimator = FlipInLeftYAnimator()
//        ora_num_recycler.itemAnimator?.apply {
//            addDuration = 500
//            removeDuration = 500
//        }

//        ora_num_recycler.addOnItemTouchListener(RecyclerItemClickListener(this.requireContext(), ora_num_recycler, object : RecyclerItemClickListener.OnItemClickListener {
//
//                    override fun onItemClick(view: View, position: Int) {
//
//                        val soundPlayed = numList.get(position)
//
//                        if (position <= 27) {
//
////                                val playa = soundPlayed.numIcon!!.toString()
////
////                                val play = Uri.parse(playa)
////
////                                playContentUri(Uri.parse(soundPlayed.numIcon.toString()))
//
//                            if (mediaPlayer == null) {
//                                mediaPlayer = MediaPlayer.create(context, soundPlayed.numIcon!!)
//                                mediaPlayer?.start()
//
//                            } else {
//                                mediaPlayer?.stop()
//                                mediaPlayer = MediaPlayer.create(context, soundPlayed.numIcon!!)
//                                mediaPlayer?.start()
//                            }
//                            Toast.makeText(activity, "LESS THAN 27", Toast.LENGTH_LONG).show()
//
//                        } else {
//
//                            playContentUri(soundPlayed.recordedAudio!!)
//                        }
//                    }
//
//                    override fun onItemLongClick(view: View?, position: Int) {
//                    }
//                })
//        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

//        val navController :NavController = Navigation.findNavController(view)

        val application = requireNotNull(activity).application
        val dao = OraWordsDatabase.invoke(application)
        val  repository = OraNumRepository(dao.oraWordsDao())
        val viewModelFactory = OraNumberViewModelFactory1(repository)

//        val viewModelFactory = OraNumberViewModelFactory(
//            OraWordsDatabase.invoke(application)
//        )

        numberViewModel = ViewModelProviders.of(this, viewModelFactory).get(OraNumberViewModel1::class.java)

        initializeList()

        val animation: Animation = AnimationUtils.loadAnimation(context, R.anim.recorder_icon_blink)

        fab1.setOnClickListener {

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
                    AUDIO_REQUEST_CODE
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

                        showToast("Select Audio Button clicked",this.context!!)

                        val chosenAudio = Uri.parse(audioFilePath)

                        if (!engText.text.isNullOrBlank() && !oraText.text.isNullOrBlank()) {

                            val engWordEntered = engText.text.toString().trim()
                            val oraWordEntered = oraText.text.toString().trim()

                            showToast("strings gotten $engWordEntered $oraWordEntered",  this.context!!)


//                            SaveOraElement(engWordEntered, oraWordEntered, null, chosenAudio)
                            SaveOraElement(engWordEntered, oraWordEntered, null, audioUri)

//                           launch {
//                                oraAdapter.addOraNumber(OraLangNums(engWordEntered, oraWordEntered, null, audioUri))
//                           }

                            showToast("New details saved", this.context!!)
                            dialog.dismiss()

                        } else {
                            showToast("Ensure you have entered the English and Ora Words", this.context!!)
                        }


                    } else if (recordButtonClicked == true) {

                        if (!engText.text.isNullOrBlank() && !oraText.text.isNullOrBlank()) {

                            val engWordEntered = engText.text.toString().trim()
                            val oraWordEntered = oraText.text.toString().trim()

                            showToast("strings gotten $engWordEntered $oraWordEntered", this.context!!)

                            SaveOraElement(engWordEntered, oraWordEntered, null, audioUri)

                            showToast("New details saved", this.context!!)
                            oraAdapter.notifyDataSetChanged()

                            dialog.dismiss()

                        } else {
                           showToast("Ensure you have entered the English and Ora Words", this.context!!)
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

//            val editText = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
//            editText.hint = "Search..."

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
                            ora_num_recycler.adapter!!.notifyDataSetChanged()
                        }
                    } else{

                        searchViewList.clear()
                        searchViewList.addAll(numList)
                        ora_num_recycler.adapter!!.notifyDataSetChanged()
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


       fun setOneTimeWorkRequest(date_p : DatePicker, time_p : ru.ifr0z.timepickercompact.TimePickerCompact ) {

        val customCalendar = java.util.Calendar.getInstance()

        customCalendar.set(
            date_p.year, date_p.month, date_p.dayOfMonth, time_p.hour, time_p.minute, 0
        )


        val customTime = customCalendar.timeInMillis
        val currentTime = currentTimeMillis()

        val delay = customTime - currentTime


        val workManager = WorkManager.getInstance(this.context!!)


        val data : Data = Data.Builder()
            .putInt(KEY_COUNT_VALUE, 125)
            .build()

        val constants = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val notificationRequest = OneTimeWorkRequest.Builder(ReminderWorker::class.java)
            .setInitialDelay(delay, MILLISECONDS)
            .setConstraints(constants)
            .setInputData(data)
            .addTag(addOraWordTag)
            .build()


        workManager.enqueue(notificationRequest)

//        workManager.beginUniqueWork(addOraWordTag, ExistingWorkPolicy.REPLACE, notificationRequest)

        workManager.getWorkInfoByIdLiveData(notificationRequest.id).observe(viewLifecycleOwner, Observer {

            val workSetStatus = it.state.name

            showToast("Status of workManager task is : $workSetStatus", this.context!!)

        })

    }
    fun SaveOraElement(engWord: String, oraWord: String, numIcon: Int?, enteredAudio: Uri) =
        launch {
            numberViewModel.saveOraElement(engWord, oraWord, numIcon, enteredAudio)
        }


    override fun onPlayOraWordClickListener(oraWords: OraLangNums) {

//        if (oraWords.oraid < 2){
            playContentUri(oraWords.recordedAudio!!, context!!)

//        }else if (oraWords.oraid>2 && <27) ) {
//
//            val  sound = oraWords.numIcon.toString()
//            val  played = Uri.parse(sound)
////            playContentInt(oraWords.numIcon!!, context!!)
//            playContentUri(played!!, context!!)
//        }
//        else{
//            playContentUri(oraWords.recordedAudio!!, context!!)
//        }
    }

    override fun onEditOraWordClickListener(oraWords: OraLangNums) {

//                              val navController : NavController = Navigation.findNavController()
//                                navController.navigate(R.id.editOraWordFragment, bundle)

                                val action = OraLangNumbersFragmentDirections.actionOraLangNumbersFragmentToEditOraWordFragment()
//                                action.oraLangNums = oraLangNumList[position]
                                    action.oraLangNums = oraWords
                                Navigation.findNavController(view!!).navigate(action)
    }




    override fun onDeleteOraWordClickListener(oraWords: OraLangNums) {
        launch {

            numberViewModel.deleteOraElement(oraWords)
        }
//        showToast("Delete icon clicked", context!!)
        showSnackbar(fab1, "${oraWords.engNum} Has been Deleted")
    }

    override fun onItemClickListener(oraWords: OraLangNums) {

    }


}


