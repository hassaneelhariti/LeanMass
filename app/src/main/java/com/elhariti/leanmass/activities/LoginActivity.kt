package com.elhariti.leanmass.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.elhariti.leanmass.database.DatabaseHelper
import com.elhariti.leanmass.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var db: DatabaseHelper
    private lateinit var prefs: SharedPreferences

    companion object {
        private const val GUEST_EMAIL = "guest@leanmass.local"
        private const val GUEST_NAME = "Invité"
        private const val GUEST_PASSWORD = "guest123"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DatabaseHelper(this)
        prefs = getSharedPreferences("leanmass_prefs", MODE_PRIVATE)

        // Si déjà connecté → aller directement à MainActivity
        if (prefs.getInt("user_id", -1) != -1) {
            goToMain()
            return
        }

        binding.btnLogin.setOnClickListener { login() }

        // Bouton SKIP - Créer un compte invité automatiquement
        binding.btnSkip.setOnClickListener { createGuestAndContinue() }

        binding.tvNoAccount.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.tvForgotPassword.setOnClickListener {
            showSnackbar("Fonctionnalité disponible prochainement.")
        }
    }

    private fun createGuestAndContinue() {
        // Vérifier si l'utilisateur invité existe déjà
        if (!db.emailExiste(GUEST_EMAIL)) {
            // Créer l'utilisateur invité
            val id = db.insertUser(GUEST_NAME, GUEST_EMAIL, GUEST_PASSWORD)
            if (id != -1L) {
                prefs.edit()
                    .putInt("user_id", id.toInt())
                    .putString("user_name", GUEST_NAME)
                    .putString("user_email", GUEST_EMAIL)
                    .apply()
                showSnackbar("Mode invité activé - vos calculs sont sauvegardés localement")
                goToMain()
            } else {
                showSnackbar("Erreur lors de l'activation du mode invité")
            }
        } else {
            // Récupérer l'utilisateur invité existant
            val guestUser = db.loginUser(GUEST_EMAIL, GUEST_PASSWORD)
            if (guestUser != null) {
                prefs.edit()
                    .putInt("user_id", guestUser.id)
                    .putString("user_name", guestUser.name)
                    .putString("user_email", guestUser.email)
                    .apply()
                showSnackbar("Mode invité activé")
                goToMain()
            } else {
                showSnackbar("Erreur lors de l'activation du mode invité")
            }
        }
    }

    private fun login() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()

        if (email.isEmpty()) {
            binding.tilEmail.error = "Entrez votre adresse e-mail"
            return
        }
        if (password.isEmpty()) {
            binding.tilPassword.error = "Entrez votre mot de passe"
            return
        }

        binding.tilEmail.error = null
        binding.tilPassword.error = null
        binding.btnLogin.isEnabled = false
        binding.progressBar.visibility = View.VISIBLE

        val user = db.loginUser(email, password)

        binding.progressBar.visibility = View.GONE
        binding.btnLogin.isEnabled = true

        if (user != null) {
            prefs.edit()
                .putInt("user_id", user.id)
                .putString("user_name", user.name)
                .putString("user_email", user.email)
                .apply()
            goToMain()
        } else {
            showSnackbar("Email ou mot de passe incorrect.")
        }
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}