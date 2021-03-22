package com.flowz.introtooralanguage.display.outdoor

import android.net.Uri
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowz.introtooralanguage.data.models.OutdoorWordsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OutdoorWordViewModel @ViewModelInject constructor(private var outdoorWordRepository: OutdoorWordRepository): ViewModel() {

    val outDoorWordsFromDb = outdoorWordRepository.outdoorWordsFromdb


    fun insertListOfOutdoorWords(outdoorWords: List<OutdoorWordsModel>){

        viewModelScope.launch (Dispatchers.IO){
            outdoorWordRepository.insertOutdoorWordS(outdoorWords)
            Log.d(TAG, "List of outdoor words Inserted into Database successfully")
        }
    }

    fun insertOutdoorWord(outdoorWord: OutdoorWordsModel){
        viewModelScope.launch (Dispatchers.IO){
            outdoorWordRepository.insertOutdoorWord(outdoorWord)
            Log.d(TAG, "Outdoor word Inserted into Database successfully")

        }
    }

    fun upDateOutdoorWord(outdoorWord: OutdoorWordsModel){
        viewModelScope.launch (Dispatchers.IO){
            outdoorWordRepository.updateOutdoorWords(outdoorWord)
            Log.d(TAG, "Outdoor word Updated into Database successfully")
        }
    }

    fun deleteOutdoorWord(outdoorWord: OutdoorWordsModel){
        viewModelScope.launch (Dispatchers.IO){
            outdoorWordRepository.deleteOutdoorWord(outdoorWord)
            Log.d(TAG, "Outdoor word Deleted from Database successfully")
        }
    }


    companion object{
        const val TAG = "OutDoorVM"
    }


}