package com.flowz.introtooralanguage.display.travel

import android.net.Uri
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.flowz.introtooralanguage.data.models.NumbersModel
import com.flowz.introtooralanguage.data.models.OutdoorWordsModel
import com.flowz.introtooralanguage.data.models.TravelWordsModel
import com.flowz.introtooralanguage.display.numbers.NumbersViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TravelWordViewModel @ViewModelInject constructor(private var travelWordRepository: TravelWordRepository): ViewModel() {

    val travelWordsFromDb = travelWordRepository.travelWordsFromdb


    fun insertListOfTravelWords(travelWords: List<TravelWordsModel>){

        viewModelScope.launch (Dispatchers.IO){
            travelWordRepository.insertTravelWordS(travelWords)
            Log.d(TAG, "List of travel words Inserted into Database successfully")
        }
    }

    fun searchTravelWords(searchQuery: String): LiveData<List<TravelWordsModel>> {
        return travelWordRepository.searchTravelWords(searchQuery).asLiveData()
        Log.d(TAG, "Searched Successfull")
    }

    fun insertTravelWord(travelWord: TravelWordsModel){
        viewModelScope.launch (Dispatchers.IO){
            travelWordRepository.insertTravelWord(travelWord)
            Log.d(TAG, "Travel word Inserted into Database successfully")

        }
    }

    fun upDateTravelrWord(travelWord: TravelWordsModel){
        viewModelScope.launch (Dispatchers.IO){
            travelWordRepository.updateTravelWord(travelWord)
            Log.d(TAG, "Travel word Updated into Database successfully")
        }
    }

    fun deleteTravelWord(travelWord: TravelWordsModel){
        viewModelScope.launch (Dispatchers.IO){
            travelWordRepository.deleteTravelWord(travelWord)
            Log.d(TAG, "Travel word Deleted from Database successfully")
        }
    }


    companion object{
        const val TAG = "TravelVM"
    }


}