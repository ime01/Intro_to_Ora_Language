package com.flowz.introtooralanguage.data

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.io.File


@Entity(tableName = "myoraWords_table")
data class OraLangNums (
    @ColumnInfo(name ="engNum")
    val engNum : String,

    @ColumnInfo(name ="oraNum")
    val oraNum : String,

    @ColumnInfo(name ="numIcon")
    val numIcon : Int?= null,

    @ColumnInfo(name ="recordedAudio")
    val recordedAudio: Uri? = null)
{
    @PrimaryKey(autoGenerate = true)
    var oraid: Int = 0

}

class UriConverters {
    @TypeConverter
    fun fromString(value: String?): Uri? {
        return if (value == null) null else Uri.parse(value)
    }

    @TypeConverter
    fun toString(uri: Uri?): String? {
        return uri.toString()
    }
}

//val numList : ArrayList<OraLangNums> = ArrayList()



