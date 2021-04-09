package com.flowz.introtooralanguage.display.numbers

import com.flowz.introtooralanguage.data.models.NumbersModel
import com.flowz.introtooralanguage.data.room.NumbersDao
import com.flowz.introtooralanguage.data.room.TravelWordsDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NumbersRepository @Inject constructor(private val numbersDao: NumbersDao) {

    val numbersFromDb = numbersDao.getNumbers()

    suspend fun insertNumber(number: NumbersModel){
        numbersDao.insert(number)
    }

    fun searchNumber(searchQuery: String): Flow<List<NumbersModel>> {
        return numbersDao.searchNumber(searchQuery)
    }

    suspend fun insertListOfNumbers(numbers: List<NumbersModel>){
        numbersDao.insertList(numbers)
    }

    suspend fun updateNumber(number: NumbersModel){
        numbersDao.update(number)
    }

    suspend fun deleteNumber(number: NumbersModel){
        numbersDao.delete(number)
    }

}