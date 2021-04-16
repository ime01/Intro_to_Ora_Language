package com.flowz.introtooralanguage.display.numbers


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
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import com.flowz.introtooralanguage.R
import com.flowz.introtooralanguage.adapters.NumbersAdapter
import com.flowz.introtooralanguage.data.models.NumbersModel
import com.flowz.introtooralanguage.extensions.playContentUri
import com.flowz.introtooralanguage.extensions.showSnackbar
import com.flowz.introtooralanguage.extensions.showToast
import com.flowz.introtooralanguage.utils.onQueryTextChanged
import com.flowz.introtooralanguage.workmanager.ReminderWorker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_ora_lang_travel.*
import kotlinx.android.synthetic.main.ora_lang_numbers.*
import java.io.IOException
import java.lang.System.currentTimeMillis
import java.util.concurrent.TimeUnit.MILLISECONDS
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */

@AndroidEntryPoint
class OraLangNumbersFragment : Fragment(), NumbersAdapter.RowClickListener {

    private val numbersViewModel by viewModels<NumbersViewModel>()

    var isRecording = false
    private val RECORD_REQUEST_CODE = 101
    private val STORAGE_REQUEST_CODE = 102
    var mediaRecorder: MediaRecorder? = null
    var mediaPlayer: MediaPlayer? = null
    lateinit var audioFilePath: String
//    lateinit var numList: ArrayList<NumbersModel>
    var searchViewList: ArrayList<NumbersModel> = ArrayList()
//    lateinit var oraAdapter: OraNumbersAdapter
    lateinit var oraAdapter: NumbersAdapter
    lateinit var uri: Uri
    lateinit var selectedPath: Uri
    var recordButtonClicked: Boolean = false
    val addOraWordTag = "addOraWordTag"


    companion object {
        const val AUDIO_REQUEST_CODE = 1
        const val KEY_COUNT_VALUE = "key_count"
        const val TAG = "NumbersFragment"
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
//        val mMediaPlayer = MediaPlayer().apply {

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

        ora_num_recycler.layoutManager = LinearLayoutManager(this.context)

        oraAdapter = NumbersAdapter(this)

//        val alphaAdapter = AlphaInAnimationAdapter(oraAdapter)
//        ora_num_recycler.adapter = ScaleInAnimationAdapter(alphaAdapter)


//        ora_num_recycler.itemAnimator = null

         numbersViewModel.numbersFromDb.observe(viewLifecycleOwner, Observer {
             oraAdapter.submitList(it)
             ora_num_recycler.adapter = oraAdapter
         })

//        ora_num_recycler.itemAnimator = FlipInLeftYAnimator()
//        ora_num_recycler.itemAnimator?.apply {
//            addDuration = 500
//            removeDuration = 500
//        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initializeList()
        swipeToDeleteNumber()

        showSnackbar(ora_num_recycler, getString(R.string.delete_info))
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

                            saveOraElement(engWordEntered, oraWordEntered, null, audioUri)

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

                            saveOraElement(engWordEntered, oraWordEntered, null, audioUri)

                            showToast("New details saved", requireContext())
                            oraAdapter.notifyDataSetChanged()

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

    private fun swipeToDeleteNumber() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val number = oraAdapter.currentList[viewHolder.adapterPosition]

                if(number.creator == 1){
                    numbersViewModel.deleteNumber(number)
                    Snackbar.make(ora_num_recycler, "Word ${number.engNum} Deleted", Snackbar.LENGTH_LONG)
                        .setAction("UNDO"){
                            numbersViewModel.insertNumber(number)
                        }.show()
                }else if (number.creator == 0){
                    showSnackbar(ora_num_recycler, "You can't delete pre-installed Ora Words")
                }


            }

        }).attachToRecyclerView(ora_num_recycler)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.menu_layout, menu)
        val menuItem = menu!!.findItem(R.id.search_oraword)
        val searchView = menuItem.actionView as SearchView

        searchView.onQueryTextChanged {
            searchDatabase(it)
            Log.d(TAG, "Search Successful")
        }

    }

    private fun searchDatabase(query:String){
        val searchQuery = "%$query%"
        numbersViewModel.searchNumber(searchQuery).observe(viewLifecycleOwner, Observer {list->
            list.let {
                oraAdapter.submitList(it)
            }

        })
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
        val currentTime = currentTimeMillis()

        val delay = customTime - currentTime

        val workManager = WorkManager.getInstance(requireContext())

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

            showToast("Status of workManager task is : $workSetStatus", requireContext())

        })

    }
    fun saveOraElement(engWord: String, oraWord: String, numIcon: Int?, enteredAudio: Uri) =

        numbersViewModel.insertNumber(NumbersModel(engWord, oraWord, numIcon,1, enteredAudio))


    override fun onPlayOraWordClickListener(number: NumbersModel) {

        number.recordedAudio?.let { playContentUri(it, requireContext()) }
    }

    override fun onEditOraWordClickListener(number: NumbersModel) {

        if (number.creator ==1){
            val action = OraLangNumbersFragmentDirections.actionOraLangNumbersFragmentToEditOraWordFragment()
//                                action.oraLangNums = oraLangNumList[position]
            action.arguments.putInt("type", 1)
            action.number = number
            Navigation.findNavController(requireView()).navigate(action)
//
        }
        else if (number.creator==0){
            showSnackbar(fab1, "You can't edit pre-installed Ora Words")
        }
    }


        override fun onItemClickListener(number: NumbersModel) {

        }


    override fun onDestroyView() {
        super.onDestroyView()
        mediaPlayer?.release()
    }

}


