package com.flowz.introtooralanguage.display.numbers

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowz.introtooralanguage.data.OraLangNums
import com.flowz.introtooralanguage.data.room.OraNumRepository
import com.flowz.introtooralanguage.data.room.OraWordsDao
import com.flowz.introtooralanguage.data.room.OraWordsDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class OraNumberViewModel1(private var oraNumRepository: OraNumRepository) : ViewModel() {


    val oraWords = oraNumRepository.oraWords

        suspend fun saveOraElement(engWord:String, oraWord: String, numIcon: Int?, enteredAudio: Uri) {

              val dbOraLang = OraLangNums(engWord, oraWord, numIcon, enteredAudio)

                oraNumRepository.insertOraWord(dbOraLang)
    }

    suspend fun updateOraElement(engWord:String, oraWord: String, numIcon: Int?, enteredAudio: Uri) {

             val dbOraLang = OraLangNums(engWord, oraWord, numIcon, enteredAudio)
            oraNumRepository.updateOraWord(dbOraLang)
    }

    suspend fun deleteOraElement(oraWord: OraLangNums) {

        oraNumRepository.deleteOraWord(oraWord)
    }

}