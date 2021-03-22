package com.flowz.introtooralanguage.di

import android.content.Context
import androidx.room.Room
import com.flowz.introtooralanguage.data.room.OraWordsDatabase
import com.flowz.introtooralanguage.data.room.OutdoorWordsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesOraDatabase(@ApplicationContext app: Context) =
        Room.databaseBuilder(app, OraWordsDatabase::class.java, DATABASENAME).build()

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