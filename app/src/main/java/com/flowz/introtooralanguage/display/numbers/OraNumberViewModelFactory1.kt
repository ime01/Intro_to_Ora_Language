package com.flowz.introtooralanguage.display.numbers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.flowz.introtooralanguage.data.room.OraNumRepository

class OraNumberViewModelFactory1 (private val repository: OraNumRepository): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return OraNumberViewModel1(repository) as T
    }
}