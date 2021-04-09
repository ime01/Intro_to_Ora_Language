package com.flowz.introtooralanguage.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.flowz.introtooralanguage.data.models.UriConverters
import com.flowz.introtooralanguage.data.models.HouseWordsModel
import com.flowz.introtooralanguage.data.models.TravelWordsModel
import kotlinx.coroutines.flow.Flow


@Dao
interface HouseWordsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert (houseWord: HouseWordsModel)

    @TypeConverters(UriConverters::class)
    @Query("SELECT * FROM housewords_table  WHERE engNum LIKE  :searchQuery OR oraNum LIKE :searchQuery")
    fun searchHouseWords(searchQuery: String): Flow<List<HouseWordsModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList (houseWords: List<HouseWordsModel>)

    @Update
    suspend fun  update(houseWord: HouseWordsModel)

    @TypeConverters(UriConverters::class)
//    @Query("select * from myoraWords_table where id = oraid ")
    @Query("SELECT * FROM  housewords_table")
    fun getHouseWords(): LiveData<List<HouseWordsModel>>

    @Delete
    suspend fun delete (houseWord: HouseWordsModel)
}