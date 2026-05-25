package com.elhariti.leanmass.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.elhariti.leanmass.R
import com.elhariti.leanmass.adapters.HistoryAdapter
import com.elhariti.leanmass.databinding.ActivityHistoryBinding
import com.elhariti.leanmass.database.DatabaseHelper
import java.util.Locale

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var db: DatabaseHelper
    private lateinit var prefs: SharedPreferences
    private lateinit var adapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DatabaseHelper(this)
        prefs = getSharedPreferences("leanmass_prefs", MODE_PRIVATE)

        setupNavigation()
        setupRecyclerView()
        loadData()
    }

    private fun setupNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_history -> true
                else -> false
            }
        }
        binding.bottomNavigation.selectedItemId = R.id.nav_history
    }

    private fun setupRecyclerView() {
        adapter = HistoryAdapter { calculId: Int ->
            db.deleteCalcul(calculId)
            loadData()
        }
        binding.rvHistory.layoutManager = LinearLayoutManager(this)
        binding.rvHistory.adapter = adapter
    }

    private fun loadData() {
        val userId = prefs.getInt("user_id", -1)
        if (userId != -1) {
            val historyList = db.getHistorique(userId)
            adapter.submitList(historyList)

            val average = db.getMoyenneLBM(userId)
            val total = db.getNombreEntrees(userId)

            binding.tvAvgLBM.text = String.format(Locale.FRANCE, "%.1f", average)
            binding.tvTotalEntries.text = total.toString()
        }
    }
}