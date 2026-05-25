package com.elhariti.leanmass.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.elhariti.leanmass.R
import com.elhariti.leanmass.adapters.HistoryAdapter
import com.elhariti.leanmass.databinding.FragmentHistoryBinding
import com.elhariti.leanmass.database.DatabaseHelper
import java.util.Locale

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: DatabaseHelper
    private lateinit var prefs: SharedPreferences
    private lateinit var adapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = DatabaseHelper(requireContext())
        prefs = requireContext().getSharedPreferences("leanmass_prefs", Context.MODE_PRIVATE)

        setupRecyclerView()
        loadData()
    }

    private fun setupRecyclerView() {
        adapter = HistoryAdapter { calculId: Int ->
            db.deleteCalcul(calculId)
            loadData()
        }
        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
