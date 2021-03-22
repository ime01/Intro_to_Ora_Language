package com.flowz.introtooralanguage.data.models

import android.net.Uri
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "housewords_table")
data class HouseWordsModel (
    @ColumnInfo(name ="engNum")
    val engNum : String,

    @ColumnInfo(name ="oraNum")
    val oraNum : String,

    @ColumnInfo(name ="recordedAudio")
    val recordedAudio: Uri? = null): Parcelable {
    @PrimaryKey(autoGenerate = true)
    var oraid: Int = 0
}

