package com.flowz.introtooralanguage.data.room

import androidx.lifecycle.LiveData
import com.flowz.introtooralanguage.data.OraLangNums

class OraNumRepository(private val dao: OraWordsDao) {

    val oraWords = dao.getOraWords()

    suspend fun insertOraWord(oraLang: OraLangNums){
        dao.insert(oraLang)
    }

    suspend fun updateOraWord(oraLang: OraLangNums){
        dao.update(oraLang)
    }

    suspend fun deleteOraWord(oraLang: OraLangNums){
        dao.delete(oraLang)
    }

}