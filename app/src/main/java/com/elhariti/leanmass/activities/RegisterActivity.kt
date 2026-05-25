package com.elhariti.leanmass.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.elhariti.leanmass.database.DatabaseHelper
import com.elhariti.leanmass.databinding.ActivityRegisterBinding

import com.google.android.material.snackbar.Snackbar

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var db: DatabaseHelper
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db    = DatabaseHelper(this)
        prefs = getSharedPreferences("leanmass_prefs", MODE_PRIVATE)

        binding.btnRegister.setOnClickListener { register() }

        binding.tvAlreadyAccount.setOnClickListener {
            finish()
        }
    }

    private fun register() {
        val name            = binding.etName.text.toString().trim()
        val email           = binding.etEmail.text.toString().trim()
        val password        = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()

        // Validation
        var isValid = true

        if (name.isEmpty()) {
            binding.tilName.error = "Entrez votre nom"
            isValid = false
        } else binding.tilName.error = null

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = "Entrez une adresse e-mail valide"
            isValid = false
        } else binding.tilEmail.error = null

        if (password.length < 6) {
            binding.tilPassword.error = "Minimum 6 caractères"
            isValid = false
        } else binding.tilPassword.error = null

        if (password != confirmPassword) {
            binding.tilConfirmPassword.error = "Les mots de passe ne correspondent pas"
            isValid = false
        } else binding.tilConfirmPassword.error = null

        if (!isValid) return

        if (db.emailExiste(email)) {
            binding.tilEmail.error = "Cet e-mail est déjà utilisé"
            return
        }

        val id = db.insertUser(name, email, password)

        if (id != -1L) {
            prefs.edit()
                .putInt("user_id", id.toInt())
                .putString("user_name", name)
                .putString("user_email", email)
                .apply()

            startActivity(Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
        } else {
            Snackbar.make(binding.root, "Erreur lors de la création du compte.", Snackbar.LENGTH_LONG).show()
        }
    }
}
