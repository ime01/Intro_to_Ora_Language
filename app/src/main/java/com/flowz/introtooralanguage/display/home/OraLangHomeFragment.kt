package com.flowz.introtooralanguage.display.home


import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.preference.PreferenceManager
//import androidx.navigation.fragment.findNavController

import com.flowz.introtooralanguage.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_ora_lang_home.*

/**
 * A simple [Fragment] subclass.
 */

//@AndroidEntryPoint
class OraLangHomeFragment : Fragment() {

    lateinit var navController :NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ora_lang_home, container, false)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().actionBar?.show()
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        navController = Navigation.findNavController(view)

        loadSettings()

        parentNum.setOnClickListener {
          navController.navigate(R.id.action_oraLangHomeFragment_to_oraLangNumbersFragment)
      }

        parentOutdoor.setOnClickListener {
            navController.navigate(R.id.action_oraLangHomeFragment_to_oraLangOutdoorFragment)
        }

        parentHouse.setOnClickListener {
            navController.navigate(R.id.action_oraLangHomeFragment_to_oraLangHouseFragment)
        }

        parentTravel.setOnClickListener {
            navController.navigate(R.id.action_oraLangHomeFragment_to_oraLangTravelFragment)
        }
    }

    private fun loadSettings() {
        val sp = PreferenceManager.getDefaultSharedPreferences(requireContext())

        val nightMode = sp.getBoolean("nightorday", false)

        if (nightMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.profile_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
      when(item.itemId){

          R.id.invite -> {
            inviteFriend()
          }

       }
        return super.onOptionsItemSelected(item)
    }


    fun inviteFriend(){
        val intent= Intent()
        intent.action=Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT,"Hey there Ora son/daughter, Come help add new words to this Ora Language App:")
        intent.type="text/plain"
        startActivity(Intent.createChooser(intent,"Invite Friend Via:"))
    }
}
