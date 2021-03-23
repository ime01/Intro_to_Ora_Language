package com.flowz.introtooralanguage.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.flowz.introtooralanguage.data.room.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesOraDatabase(@ApplicationContext app: Context, houseDaoProvider: Provider<HouseWordsDao>, numbersDaoProvider: Provider<NumbersDao>, outdoorDaoProvider: Provider<OutdoorWordsDao>, travelDaoProvider: Provider<TravelWordsDao>) =
        Room.databaseBuilder(app, OraWordsDatabase::class.java, DATABASENAME).addCallback(object: RoomDatabase.Callback(){
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                val numList = DefaultDataSource().ListOfNumbers(app)
                val travelList = DefaultDataSource().ListOfTravelWords(app)
                val houseList = DefaultDataSource().ListOfHouseWords(app)
                val outdoorList = DefaultDataSource().ListOfOudoorWords(app)


                GlobalScope.launch (Dispatchers.IO){

                    houseDaoProvider.get().insertList(houseList)
                    outdoorDaoProvider.get().insertList(outdoorList)
                    numbersDaoProvider.get().insertList(numList)
                    travelDaoProvider.get().insertList(travelList)

                }

            }

        }).fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun providesOutdoorWordsDao(db : OraWordsDatabase) = db.outDoorWordsDao()

    @Provides
    @Singleton
    fun providesTravelWordsDao(db: OraWordsDatabase) = db.TravelWordsDao()

    @Provides
    @Singleton
    fun providesHouseWordsDao(db: OraWordsDatabase) = db.houseWordsDao()

    @Provides
    @Singleton
    fun providesNumbesDao(db: OraWordsDatabase) = db.NumbersDao()

}

const val DATABASENAME = "allorawords.db"