package com.flowz.introtooralanguage.display.house

import com.flowz.introtooralanguage.data.models.HouseWordsModel
import com.flowz.introtooralanguage.data.models.NumbersModel
import com.flowz.introtooralanguage.data.room.OutdoorWordsDao
import com.flowz.introtooralanguage.data.models.OutdoorWordsModel
import com.flowz.introtooralanguage.data.room.HouseWordsDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HouseRepository @Inject constructor(private val houseDao: HouseWordsDao ) {

    val houseWordsFromdb = houseDao.getHouseWords()

    suspend fun insertHouseWord(houseword: HouseWordsModel){
        houseDao.insert(houseword)
    }

    fun searchHouseWords(searchQuery: String): Flow<List<HouseWordsModel>> {
        return houseDao.searchHouseWords(searchQuery)
    }

    suspend fun insertHouseWordS(listOfhouseWords: List<HouseWordsModel> ){
        houseDao.insertList(listOfhouseWords)
    }

    suspend fun updateHouseWord(houseword: HouseWordsModel){
        houseDao.update(houseword)
    }

    suspend fun deleteHouseWord(houseword: HouseWordsModel){
        houseDao.delete(houseword)
    }

}