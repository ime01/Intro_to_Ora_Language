package com.flowz.introtooralanguage.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.flowz.introtooralanguage.R
import kotlinx.coroutines.*

class SplashFragment : Fragment() {

    val activityScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        activityScope.launch {
            delay(3000)

            if (onBoardingFinished()){
                findNavController(requireParentFragment()).navigate(R.id.action_splashFragment_to_loginActivity)
            }else{
                findNavController(requireParentFragment()).navigate(R.id.action_splashFragment_to_viewPagerFragment)
            }
        }

        return inflater.inflate(R.layout.fragment_splash, container, false)
    }


    private fun onBoardingFinished():Boolean{
        val sharedPref =requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Finished", false)
    }

    override fun onPause() {
        activityScope.cancel()
        super.onPause()
    }
}