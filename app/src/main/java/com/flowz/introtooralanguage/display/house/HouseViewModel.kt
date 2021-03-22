package com.flowz.introtooralanguage.display.house

import android.net.Uri
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowz.introtooralanguage.data.models.HouseWordsModel
import com.flowz.introtooralanguage.data.models.OutdoorWordsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HouseViewModel @ViewModelInject constructor(private var houseRepository: HouseRepository): ViewModel() {

    val houseWordsFromDb = houseRepository.houseWordsFromdb


    fun insertListOfHouseWords(houseWords: List<HouseWordsModel>){

        viewModelScope.launch (Dispatchers.IO){
            houseRepository.insertHouseWordS(houseWords)
            Log.d(TAG, "List of House words Inserted into Database successfully")
        }
    }

    fun insertHouseWord(houseword: HouseWordsModel){
        viewModelScope.launch (Dispatchers.IO){
            houseRepository.insertHouseWord(houseword)
            Log.d(TAG, "House word Inserted into Database successfully")

        }
    }

    fun upDateHouseWord(houseword: HouseWordsModel){
        viewModelScope.launch (Dispatchers.IO){
          houseRepository.updateHouseWord(houseword)
            Log.d(TAG, "House word Updated into Database successfully")
        }
    }

    fun deleteHouseWord(houseword: HouseWordsModel){
        viewModelScope.launch (Dispatchers.IO){
            houseRepository.deleteHouseWord(houseword)
            Log.d(TAG, "House word Deleted from Database successfully")
        }
    }


    companion object{
        const val TAG = "HouseVM"
    }


}