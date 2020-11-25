package com.flowz.introtooralanguage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.Navigation.findNavController

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(this, R.id.nav_host_fragment)

        setupActionBarWithNavController(this, navController)

//        setupActionBarWithNavController( findNavController(R.id.nav_host_fragment))

    }

    override fun onSupportNavigateUp(): Boolean {

        val navController = findNavController(this, R.id.nav_host_fragment)
        return navController.navigateUp()|| super.onSupportNavigateUp()
    }

}
