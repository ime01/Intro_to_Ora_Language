package com.flowz.introtooralanguage.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.flowz.introtooralanguage.data.models.UriConverters
import com.flowz.introtooralanguage.data.models.HouseWordsModel


@Dao
interface HouseWordsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert (houseWord: HouseWordsModel)

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