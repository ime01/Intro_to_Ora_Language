package com.flowz.introtooralanguage.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.flowz.introtooralanguage.data.models.NumbersModel
import com.flowz.introtooralanguage.data.models.UriConverters
import com.flowz.introtooralanguage.data.models.TravelWordsModel
import kotlinx.coroutines.flow.Flow


@Dao
interface TravelWordsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert (travelWord: TravelWordsModel)
    @TypeConverters(UriConverters::class)
    @Query("SELECT * FROM travelwords_table  WHERE engNum LIKE  :searchQuery OR oraNum LIKE :searchQuery")
    fun searchTravelWords(searchQuery: String): Flow<List<TravelWordsModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList (travelWord: List<TravelWordsModel>)

    @Update
    suspend fun  update(travelWord: TravelWordsModel)

    @TypeConverters(UriConverters::class)
//    @Query("select * from myoraWords_table where id = oraid ")
    @Query("SELECT * FROM  travelwords_table")
    fun getTravelWords(): LiveData<List<TravelWordsModel>>

    @Delete
    suspend fun delete (travelWord: TravelWordsModel)
}