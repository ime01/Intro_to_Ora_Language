package com.flowz.introtooralanguage.display.numbers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.flowz.introtooralanguage.data.room.OraWordsDatabase

class OraNumberViewModelFactory (private val oradb: OraWordsDatabase): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return OraNumberViewModel(oradb) as T
    }
}