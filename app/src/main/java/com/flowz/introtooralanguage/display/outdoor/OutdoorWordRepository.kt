package com.flowz.introtooralanguage.display.outdoor

import com.flowz.introtooralanguage.data.models.NumbersModel
import com.flowz.introtooralanguage.data.room.OutdoorWordsDao
import com.flowz.introtooralanguage.data.models.OutdoorWordsModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OutdoorWordRepository @Inject constructor(private val outdoorDao: OutdoorWordsDao ) {

    val outdoorWordsFromdb = outdoorDao.getOutdoorWords()

    suspend fun insertOutdoorWord(outdoorWord: OutdoorWordsModel){
        outdoorDao.insert(outdoorWord)
    }

    fun searchOutdoorWords(searchQuery: String): Flow<List<OutdoorWordsModel>> {
        return outdoorDao.searchOutdoorWord(searchQuery)
    }

    suspend fun insertOutdoorWordS(listOfoutdoorWords: List<OutdoorWordsModel> ){
        outdoorDao.insertList(listOfoutdoorWords)
    }

    suspend fun updateOutdoorWords(outdoorWord: OutdoorWordsModel){
        outdoorDao.update(outdoorWord)
    }

    suspend fun deleteOutdoorWord(outdoorWord: OutdoorWordsModel){
        outdoorDao.delete(outdoorWord)
    }

}