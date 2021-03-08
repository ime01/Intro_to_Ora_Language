package com.flowz.introtooralanguage.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.flowz.introtooralanguage.R
import com.flowz.introtooralanguage.onboarding.screens.FirstonboardFragment
import com.flowz.introtooralanguage.onboarding.screens.SecondOnboardingFragment
import com.flowz.introtooralanguage.onboarding.screens.ThirdOnboardFragment
import kotlinx.android.synthetic.main.fragment_view_pager.*
import kotlinx.android.synthetic.main.fragment_view_pager.view.*


class ViewPagerFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
       val view = inflater.inflate(R.layout.fragment_view_pager, container, false)

        val fragmentList = arrayListOf<Fragment>( FirstonboardFragment(), SecondOnboardingFragment(), ThirdOnboardFragment())

        val adapter = ViewPagerAdapter(fragmentList, requireActivity().supportFragmentManager, lifecycle)

       view.view_pager2.adapter = adapter

        return view
    }


}