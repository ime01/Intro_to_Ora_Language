package com.flowz.introtooralanguage.display.numbers

import android.net.Uri
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.flowz.introtooralanguage.data.models.NumbersModel
import com.flowz.introtooralanguage.data.models.TravelWordsModel
import com.flowz.introtooralanguage.display.travel.TravelWordRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NumbersViewModel @ViewModelInject constructor(private var numbersRepository: NumbersRepository): ViewModel() {

    val numbersFromDb = numbersRepository.numbersFromDb


    fun insertListOfNumbers(numbers:List<NumbersModel> ){

        viewModelScope.launch (Dispatchers.IO){
            numbersRepository.insertListOfNumbers(numbers)
            Log.d(TAG, "List of Numbers Inserted into Database successfully")
        }
    }

    fun searchNumber(searchQuery: String): LiveData<List<NumbersModel>> {
        return numbersRepository.searchNumber(searchQuery).asLiveData()
        Log.d(TAG, "Searched Successfull")
    }


    fun insertNumber(number: NumbersModel){
        viewModelScope.launch (Dispatchers.IO){
            numbersRepository.insertNumber(number)
            Log.d(TAG, "Number Inserted into Database successfully")

        }
    }

    fun upDateNumber(number: NumbersModel){
        viewModelScope.launch (Dispatchers.IO){
            numbersRepository.updateNumber(number)
            Log.d(TAG, "Number Updated into Database successfully")
        }
    }

    fun deleteNumber(number: NumbersModel){
        viewModelScope.launch (Dispatchers.IO){
            numbersRepository.deleteNumber(number)
            Log.d(TAG, "Number Deleted from Database successfully")
        }
    }


    companion object{
        const val TAG = "NumbersVM"
    }


}