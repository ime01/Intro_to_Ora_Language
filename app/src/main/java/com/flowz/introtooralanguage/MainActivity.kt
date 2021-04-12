package com.flowz.introtooralanguage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.navigateUp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        navController = findNavController(this, R.id.nav_host_fragment)

        NavigationUI.setupWithNavController(navigation_view, navController)
        NavigationUI.setupActionBarWithNavController(this, navController, drawer_layout)

//
//        val navController = findNavController(this, R.id.nav_host_fragment)
//
//        setupActionBarWithNavController(this, navController)

//        setupActionBarWithNavController( findNavController(R.id.nav_host_fragment))

    }

    override fun onSupportNavigateUp(): Boolean {

//        val navController = findNavController(this, R.id.nav_host_fragment)
//        return navController.navigateUp()|| super.onSupportNavigateUp()
       return NavigationUI.navigateUp(navController, drawer_layout)
    }

}
