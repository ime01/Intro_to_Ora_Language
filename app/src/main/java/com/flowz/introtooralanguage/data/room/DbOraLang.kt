package com.flowz.introtooralanguage.data.room

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "oraWords_table")
data class DbOraLang (

    val engNum : String,
    val oraNum : String,
    val numIcon : Int?= null,
    val recordedAudio: Uri? = null)
{
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0

}



