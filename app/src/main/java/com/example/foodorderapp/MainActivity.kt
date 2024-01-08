package com.example.foodorderapp

import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.foodorderapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize NavHostFragment
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        window.statusBarColor = Color.parseColor("#FFFFFF")
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.navigationBarColor = Color.parseColor("#FFFFFF")

        binding.bottomnav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> navigateToFragment(R.id.homeFragment)
                R.id.search -> navigateToFragment(R.id.searchFragment)
                R.id.cart -> navigateToFragment(R.id.cartFragment)
                else -> return@setOnItemSelectedListener false
            }
            true
        }

        // Update bottom navigation selected item based on destination changes
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> binding.bottomnav.menu.findItem(R.id.home)?.isChecked = true
                R.id.searchFragment -> binding.bottomnav.menu.findItem(R.id.search)?.isChecked = true
                R.id.cartFragment -> binding.bottomnav.menu.findItem(R.id.cart)?.isChecked = true
            }
        }
    }

    private fun navigateToFragment(fragmentId: Int) {
        if (navController.currentDestination?.id != fragmentId) {
            navController.navigate(fragmentId)
        }
    }

    override fun onBackPressed() {
        val currentDestinationId = navController.currentDestination?.id

        if (currentDestinationId == R.id.homeFragment) {
            // If the current destination is the home fragment, close the app
            finish()
        } else {
            // If it's not the home fragment, navigate back as usual
            super.onBackPressed()
        }
    }
}
