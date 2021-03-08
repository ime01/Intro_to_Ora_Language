package com.flowz.introtooralanguage.onboarding.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.flowz.introtooralanguage.R
import com.flowz.introtooralanguage.firebase.LoginActivity
import kotlinx.android.synthetic.main.fragment_third_onboard.*
import kotlinx.android.synthetic.main.fragment_third_onboard.view.*


class ThirdOnboardFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_third_onboard, container, false)

        view.finish.setOnClickListener {

            val intent = Intent(getActivity(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()

          onBoardingFinished()
        }

        return view
    }


    private fun onBoardingFinished(){
        val sharedPref =requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Finished", true)
        editor.apply()
    }

}