package com.flowz.introtooralanguage.onboarding.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.flowz.introtooralanguage.R
import kotlinx.android.synthetic.main.fragment_firstonboard.view.*

class FirstonboardFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_firstonboard, container, false)

        val viewPager = activity?.findViewById<ViewPager2>(R.id.view_pager2)

        view.next1.setOnClickListener {
           viewPager?.currentItem = 1
        }

        return view

    }

}