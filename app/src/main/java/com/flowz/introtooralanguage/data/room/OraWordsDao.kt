package com.flowz.introtooralanguage.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.flowz.introtooralanguage.data.OraLangNums
import com.flowz.introtooralanguage.data.UriConverters


@Dao
interface OraWordsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert (dboraLang: OraLangNums)

    @TypeConverters(UriConverters::class)
    @Query("select * from myoraWords_table where id = id ")
    fun getOraWords(): LiveData<List<OraLangNums>>
//    fun getOraWords(): LiveData<ArrayList<OraLangNums>>

    @Delete
    fun delete (dboraLang: OraLangNums)
}