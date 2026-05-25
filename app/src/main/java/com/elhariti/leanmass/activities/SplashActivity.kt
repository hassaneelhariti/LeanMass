package com.elhariti.leanmass.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.elhariti.leanmass.R

class SplashActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        prefs = getSharedPreferences("leanmass_prefs", MODE_PRIVATE)

        // Attendre 2 secondes puis vérifier la connexion
        Handler(Looper.getMainLooper()).postDelayed({
            checkLoginStatus()
        }, 2000)
    }

    private fun checkLoginStatus() {
        val userId = prefs.getInt("user_id", -1)

        if (userId != -1) {
            // Utilisateur connecté -> aller à MainActivity
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            // Utilisateur non connecté -> aller à LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
        }
        finish()
    }
}