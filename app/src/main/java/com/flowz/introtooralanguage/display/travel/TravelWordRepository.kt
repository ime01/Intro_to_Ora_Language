package com.flowz.introtooralanguage.display.travel

import com.flowz.introtooralanguage.data.room.OutdoorWordsDao
import com.flowz.introtooralanguage.data.models.OutdoorWordsModel
import com.flowz.introtooralanguage.data.models.TravelWordsModel
import com.flowz.introtooralanguage.data.room.TravelWordsDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TravelWordRepository @Inject constructor(private val travelDao: TravelWordsDao ) {

    val travelWordsFromdb = travelDao.getTravelWords()

    suspend fun insertTravelWord(travelWord: TravelWordsModel){
        travelDao.insert(travelWord)
    }

    suspend fun insertTravelWordS(listOftravelWords: List<TravelWordsModel> ){
        travelDao.insertList(listOftravelWords)
    }

    suspend fun updateTravelWord(travelWord: TravelWordsModel){
        travelDao.update(travelWord)
    }

    suspend fun deleteTravelWord(travelWord: TravelWordsModel){
        travelDao.delete(travelWord)
    }

}