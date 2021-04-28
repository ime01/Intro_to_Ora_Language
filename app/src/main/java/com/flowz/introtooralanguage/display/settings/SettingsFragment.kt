package com.flowz.introtooralanguage.display.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.flowz.introtooralanguage.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}