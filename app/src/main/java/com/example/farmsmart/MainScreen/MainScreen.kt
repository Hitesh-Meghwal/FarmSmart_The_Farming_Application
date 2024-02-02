package com.example.farmsmart.MainScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.farmsmart.R
import com.example.farmsmart.databinding.ActivityMainScreenBinding

class MainScreen : AppCompatActivity() {
    lateinit var binding: ActivityMainScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomnav = binding.bottomNavigationView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        bottomnav.setupWithNavController(navController)


    }
}