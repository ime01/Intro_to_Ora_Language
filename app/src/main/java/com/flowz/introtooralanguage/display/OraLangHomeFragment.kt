package com.flowz.introtooralanguage.display


import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
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

        val navController :NavController = Navigation.findNavController(view)

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.profile_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
      when(item.itemId){

            R.id.ora_user_profile -> {
                val navController :NavController = Navigation.findNavController(requireView())
                navController.navigate(R.id.action_oraLangHomeFragment_to_userProfileFragment)
            }
       }
        return super.onOptionsItemSelected(item)
    }
}
