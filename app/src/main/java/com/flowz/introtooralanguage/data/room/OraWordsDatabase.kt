package com.flowz.introtooralanguage.data.room

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.flowz.introtooralanguage.data.models.*

@Database(entities = [NumbersModel::class, OutdoorWordsModel::class, TravelWordsModel::class, HouseWordsModel::class], version = 7, exportSchema = false)
@TypeConverters(UriConverters::class)
abstract class OraWordsDatabase : RoomDatabase(){

    abstract fun houseWordsDao(): HouseWordsDao
    abstract fun outDoorWordsDao(): OutdoorWordsDao
    abstract fun TravelWordsDao(): TravelWordsDao
    abstract fun NumbersDao(): NumbersDao

    companion object{

        @Volatile private var instance: OraWordsDatabase? = null
        private var LOCK = Any()

        operator fun invoke (context: Context) = instance?: synchronized(LOCK){

            instance ?: buidDatabase(context).also {
                instance = it
            }
        }

        private fun buidDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, OraWordsDatabase::class.java, "orawords.db").fallbackToDestructiveMigration().build()


    }


}