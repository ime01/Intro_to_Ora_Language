package com.flowz.introtooralanguage.display.house


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
import com.flowz.introtooralanguage.data.models.HouseWordsModel
import com.flowz.introtooralanguage.display.numbers.NumbersRepository
import com.flowz.introtooralanguage.data.room.OraWordsDatabase
import com.flowz.introtooralanguage.display.base.ScopedFragment
import com.flowz.introtooralanguage.display.numbers.OraLangNumbersFragment
import com.flowz.introtooralanguage.extensions.playContentUri
import com.flowz.introtooralanguage.extensions.showSnackbar
//import com.flowz.introtooralanguage.display.numbers.OraNumberViewModel
//import com.flowz.introtooralanguage.display.numbers.OraNumberViewModelFactory
import com.flowz.introtooralanguage.extensions.showToast
import com.flowz.introtooralanguage.workmanager.ReminderWorker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_ora_lang_house.*
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */

@AndroidEntryPoint
class OraLangHouseFragment : Fragment(), HouseWordsAdapter.RowClickListener {

    private val houseViewModel by viewModels<HouseViewModel>()

    var isRecording = false
    private val RECORD_REQUEST_CODE = 101
    private val STORAGE_REQUEST_CODE = 102
    var mediaRecorder: MediaRecorder? = null
    var mediaPlayer: MediaPlayer? = null
    lateinit var audioFilePath: String
    lateinit var numList: ArrayList<HouseWordsModel>
    var searchViewList: ArrayList<HouseWordsModel> = ArrayList()
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


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ora_lang_house, container, false)
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
        showToast("Recording Stoped", this.requireContext())
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
            showToast(
                "No microphone to reocrd, try selecting an Audio file instead",
                this.requireContext()
            )
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
            showToast("Error in getting music file", this.requireContext())
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
        val houseList: ArrayList<HouseWordsModel> = ArrayList()

        houseList.add(0, HouseWordsModel("Welcome", "O bo khian", Uri.parse("android.resource://"+context?.packageName  +"/raw/one")))
        houseList.add(1, HouseWordsModel("Switch it on", "Ror ee ruu", Uri.parse("android.resource://"+context?.packageName  +"/raw/one")))
        houseList.add(2, HouseWordsModel("Put it down", "Muii khi vbo otoee", Uri.parse("android.resource://"+context?.packageName  +"/raw/one")))
        houseList.add(3, HouseWordsModel("Open the door", "Thu khu khe dee aah", Uri.parse("android.resource://"+context?.packageName  +"/raw/one")))
        houseList.add(4, HouseWordsModel("Close the door", "Ghu khu khe dee aah", Uri.parse("android.resource://"+context?.packageName  +"/raw/one")))
        houseList.add(5, HouseWordsModel("Go do the dishes", "Oho doh kpee ta saa", Uri.parse("android.resource://"+context?.packageName  +"/raw/one")))
        houseList.add(6, HouseWordsModel("Sit Down", "Dee gha vbo toie", Uri.parse("android.resource://"+context?.packageName  +"/raw/one")))
        houseList.add(7, HouseWordsModel("Stand up", "Kparhe muze", Uri.parse("android.resource://"+context?.packageName  +"/raw/one")))
        houseList.add(8, HouseWordsModel("Come and eat", " Vie  ebaee", Uri.parse("android.resource://"+context?.packageName  +"/raw/one")))
        houseList.add(9, HouseWordsModel("Be carefull", "Fuen gbee rhe", Uri.parse("android.resource://"+context?.packageName  +"/raw/one")))
        houseList.add(10, HouseWordsModel("Go buy a loaf of bread", "Olo odor doo okoh oibo", Uri.parse("android.resource://"+context?.packageName  +"/raw/one")))
        searchViewList.addAll(houseList)

        houseViewModel.insertListOfHouseWords(houseList)

        ora_house_recycler.layoutManager = LinearLayoutManager(this.context)


       val oraAdapter = HouseWordsAdapter(this)


        val animation: Animation = AnimationUtils.loadAnimation(context, R.anim.recorder_icon_blink)

        houseViewModel.houseWordsFromDb.observe(viewLifecycleOwner, Observer {
            oraAdapter.submitList(it)
            ora_house_recycler.adapter = oraAdapter
        })


        fab3.setOnClickListener {

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
                showToast("Audio disgarded", this.requireContext())

            }

            val alertDialog = MaterialAlertDialogBuilder(this.requireContext())
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

                        showToast("Select Audio Button clicked", this.requireContext())

                        val chosenAudio = Uri.parse(audioFilePath)

                        if (!engText.text.isNullOrBlank() && !oraText.text.isNullOrBlank()) {

                            val engWordEntered = engText.text.toString().trim()
                            val oraWordEntered = oraText.text.toString().trim()

                            showToast("strings gotten $engWordEntered $oraWordEntered", this.requireContext())

                            SaveOraElement(engWordEntered, oraWordEntered, chosenAudio)

//
                            showToast("New details saved", this.requireContext())
                            dialog.dismiss()

                        } else {
                            showToast("Ensure you have entered the English and Ora Words", this.requireContext())
                        }


                    } else if (recordButtonClicked == true) {

                        if (!engText.text.isNullOrBlank() && !oraText.text.isNullOrBlank()) {

                            val engWordEntered = engText.text.toString().trim()
                            val oraWordEntered = oraText.text.toString().trim()

                            showToast("strings gotten $engWordEntered $oraWordEntered", this.requireContext())

                            SaveOraElement(engWordEntered, oraWordEntered,  audioUri)

                            showToast("New details saved", this.requireContext())

                            dialog.dismiss()

                        } else {
                            showToast(
                                "Ensure you have entered the English and Ora Words",
                                this.requireContext()
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
                            ora_house_recycler.adapter!!.notifyDataSetChanged()
                        }
                    } else{

                        searchViewList.clear()
                        searchViewList.addAll(numList)
                        ora_house_recycler.adapter!!.notifyDataSetChanged()
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


        val alertDialog = MaterialAlertDialogBuilder(this.requireContext())
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

            showToast("Status of workManager task is : $workSetStatus", this.requireContext())

        })

    }
    fun SaveOraElement(engWord: String, oraWord: String, enteredAudio: Uri) =
       houseViewModel.insertHouseWord(HouseWordsModel(engWord, oraWord, enteredAudio))

    override fun onPlayOraWordClickListener(houseWord: HouseWordsModel) {
        houseWord.recordedAudio?.let { playContentUri(it, requireContext()) }
    }

    override fun onEditOraWordClickListener(houseWord: HouseWordsModel) {

        if (houseWord.oraid<10){
            showSnackbar(fab3, "You Cant't edit pre-installed OraWords")
        }else if (houseWord.oraid>10){
            val action = OraLangHouseFragmentDirections.actionOraLangHouseFragmentToEditOraWordFragment()
//                                action.oraLangNums = oraLangNumList[position]
            action.arguments.putInt("type", 2)
            action.houseWord = houseWord
            Navigation.findNavController(requireView()).navigate(action)
        }

    }

    override fun onDeleteOraWordClickListener(houseWord: HouseWordsModel) {
        houseViewModel.deleteHouseWord(houseWord)
    }

    override fun onItemClickListener(houseWord: HouseWordsModel) {

    }

}
