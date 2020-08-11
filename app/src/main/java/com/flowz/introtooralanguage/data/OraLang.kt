package com.flowz.introtooralanguage.data

import android.net.Uri
import java.io.File

data class OraLangNums ( val engNum : String, val oraNum : String, val numIcon : Int?= null, val recordedAudio: Uri? = null )

val numList : ArrayList<OraLangNums> = ArrayList()



