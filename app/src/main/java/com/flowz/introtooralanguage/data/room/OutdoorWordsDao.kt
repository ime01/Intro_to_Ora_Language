package com.flowz.introtooralanguage.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.flowz.introtooralanguage.data.models.UriConverters
import com.flowz.introtooralanguage.data.models.OutdoorWordsModel


@Dao
interface OutdoorWordsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert (outdoorWord: OutdoorWordsModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList (outdoorWords: List<OutdoorWordsModel>)

    @Update
    suspend fun  update(outdoorWord: OutdoorWordsModel)

    @TypeConverters(UriConverters::class)
//    @Query("select * from myoraWords_table where id = oraid ")
    @Query("SELECT * FROM  outdoorwords_table")
    fun getOutdoorWords(): LiveData<List<OutdoorWordsModel>>

    @Delete
    suspend fun delete (outdoorWord: OutdoorWordsModel)
}