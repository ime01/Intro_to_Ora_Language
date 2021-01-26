package com.flowz.introtooralanguage.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.flowz.introtooralanguage.data.OraLangNums
import com.flowz.introtooralanguage.data.UriConverters


@Dao
interface OraWordsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert (dboraLang: OraLangNums)

    @Update
    suspend fun  update(dboraLang: OraLangNums)

    @TypeConverters(UriConverters::class)
//    @Query("select * from myoraWords_table where id = oraid ")
    @Query("SELECT * FROM  myoraWords_table")
    fun getOraWords(): LiveData<List<OraLangNums>>

    @Delete
    suspend fun delete (dboraLang: OraLangNums)
}