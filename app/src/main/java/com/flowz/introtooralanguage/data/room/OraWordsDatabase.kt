package com.flowz.introtooralanguage.data.room

import android.content.Context
import androidx.room.*
import com.flowz.introtooralanguage.data.OraLangNums
import com.flowz.introtooralanguage.data.UriConverters

@Database(entities = [OraLangNums::class], version = 1)
@TypeConverters(UriConverters::class)
abstract class OraWordsDatabase : RoomDatabase(){

    abstract fun oraWordsDao(): OraWordsDao

    companion object{

        @Volatile private var instance: OraWordsDatabase? = null
        private var LOCK = Any()

        operator fun invoke (context: Context) = instance?: synchronized(LOCK){

            instance ?: buidDatabase(context).also {
                instance = it
            }
        }

        private fun buidDatabase(context: Context) = Room.databaseBuilder(context.applicationContext, OraWordsDatabase::class.java, "orawords.db").build()


    }


}