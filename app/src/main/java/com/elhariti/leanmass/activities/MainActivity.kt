package com.elhariti.leanmass.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.elhariti.leanmass.R
import com.elhariti.leanmass.databinding.ActivityMainBinding
import com.elhariti.leanmass.fragments.CalculatorFragment
import com.elhariti.leanmass.fragments.HistoryFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Charger le fragment par défaut (Calculator)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, CalculatorFragment())
                .commit()
        }

        setupNavigation()
    }

    private fun setupNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, CalculatorFragment())
                        .commit()
                    true
                }
                R.id.nav_history -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, HistoryFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
    }
}