package com.elhariti.leanmass.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.elhariti.leanmass.databinding.ItemHistoryBinding
import com.elhariti.leanmass.models.Calcul
import java.util.Locale

class HistoryAdapter(
    private val onDeleteClick: (Int) -> Unit
) : ListAdapter<Calcul, HistoryAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding, onDeleteClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemHistoryBinding,
        private val onDeleteClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(calcul: Calcul) {
            binding.tvLBMValue.text = String.format(Locale.FRANCE, "%.1f kg", calcul.lbm)
            binding.tvPoids.text = String.format(Locale.FRANCE, "%.1f kg", calcul.poids)
            binding.tvTaille.text = String.format(Locale.FRANCE, "%d cm", calcul.taille)
            binding.tvDate.text = calcul.date

            val sexeText = if (calcul.sexe.name == "HOMME") "Homme" else "Femme"
            binding.tvSexe.text = sexeText

            binding.btnDelete.setOnClickListener {
                onDeleteClick(calcul.id)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Calcul>() {
        override fun areItemsTheSame(oldItem: Calcul, newItem: Calcul): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Calcul, newItem: Calcul): Boolean {
            return oldItem == newItem
        }
    }
}