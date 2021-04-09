package com.flowz.introtooralanguage.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.flowz.introtooralanguage.data.models.HouseWordsModel
import com.flowz.introtooralanguage.data.models.NumbersModel
import com.flowz.introtooralanguage.data.models.UriConverters
import kotlinx.coroutines.flow.Flow


@Dao
interface NumbersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert (number: NumbersModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList (numbers: List<NumbersModel>)

    @Update
    suspend fun  update(number: NumbersModel)

    @TypeConverters(UriConverters::class)
//    @Query("select * from myoraWords_table where id = oraid ")
    @Query("SELECT * FROM  numbers_table")
    fun getNumbers(): LiveData<List<NumbersModel>>

    @TypeConverters(UriConverters::class)
    @Query("SELECT * FROM numbers_table  WHERE engNum LIKE  :searchQuery OR oraNum LIKE :searchQuery")
    fun searchNumber(searchQuery: String): Flow<List<NumbersModel>>


    @Delete
    suspend fun delete (number: NumbersModel)
}