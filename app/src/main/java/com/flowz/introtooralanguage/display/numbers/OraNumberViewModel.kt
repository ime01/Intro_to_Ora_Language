package com.flowz.introtooralanguage.display.numbers

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowz.introtooralanguage.data.OraLangNums
import com.flowz.introtooralanguage.data.room.OraWordsDao
import com.flowz.introtooralanguage.data.room.OraWordsDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class OraNumberViewModel(private var oraDb: OraWordsDatabase) : ViewModel() {


        fun SaveOraElement(engWord:String, oraWord: String, numIcon: Int?, enteredAudio: Uri) {


        val dbOraLang = OraLangNums(engWord, oraWord, numIcon, enteredAudio)

        oraDb.oraWordsDao().insert(dbOraLang)

    }


      fun getSavedOraWords():LiveData<List<OraLangNums>>{

            return   oraDb.oraWordsDao().getOraWords()
     }

}