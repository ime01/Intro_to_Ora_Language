package com.flowz.introtooralanguage.data.models

import android.net.Uri
import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize


data class FbNumbersModel (

    val engNum : String = " ",

    val oraNum : String = " ",

    val numIcon : Int?= 0,

    var creator : Int?= 0,

    val recordedAudio: String? = " ")




