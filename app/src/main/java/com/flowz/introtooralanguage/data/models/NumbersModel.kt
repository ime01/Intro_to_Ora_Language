package com.flowz.introtooralanguage.data.models

import android.net.Uri
import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "numbers_table")
data class NumbersModel (
    @ColumnInfo(name ="engNum")
    val engNum : String,

    @ColumnInfo(name ="oraNum")
    val oraNum : String,

    @ColumnInfo(name ="numIcon")
    val numIcon : Int?= null,

    @ColumnInfo(name ="recordedAudio")
    val recordedAudio: Uri? = null):Parcelable {
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



