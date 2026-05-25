package com.elhariti.leanmass.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.elhariti.leanmass.R
import com.elhariti.leanmass.database.DatabaseHelper
import com.elhariti.leanmass.databinding.FragmentCalculatorBinding
import com.elhariti.leanmass.models.Calcul
import com.elhariti.leanmass.models.Sexe
import com.elhariti.leanmass.utils.LBMCalculator
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

class CalculatorFragment : Fragment() {

    private var _binding: FragmentCalculatorBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: DatabaseHelper
    private lateinit var prefs: SharedPreferences
    private var dernierCalcul: Calcul? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalculatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = DatabaseHelper(requireContext())
        prefs = requireContext().getSharedPreferences("leanmass_prefs", Context.MODE_PRIVATE)

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnCalculer.setOnClickListener { calculer() }
        binding.btnEnregistrer.setOnClickListener { enregistrer() }
        binding.btnPartager.setOnClickListener { partager() }
    }

    private fun calculer() {
        val poidsStr = binding.etPoids.text.toString().trim()
        val tailleStr = binding.etTaille.text.toString().trim()

        if (poidsStr.isEmpty()) {
            binding.tilPoids.error = "Entrez votre poids"
            return
        }
        if (tailleStr.isEmpty()) {
            binding.tilTaille.error = "Entrez votre taille"
            return
        }

        binding.tilPoids.error = null
        binding.tilTaille.error = null

        val poids = poidsStr.toDoubleOrNull() ?: return
        val taille = tailleStr.toIntOrNull() ?: return
        val sexe = if (binding.btnHomme.isChecked) Sexe.HOMME else Sexe.FEMME

        if (poids <= 0 || poids > 300) {
            binding.tilPoids.error = "Poids invalide (1-300 kg)"
            return
        }
        if (taille <= 0 || taille > 250) {
            binding.tilTaille.error = "Taille invalide (1-250 cm)"
            return
        }

        val seuilHomme = 38.0
        val seuilFemme = 24.0

        val lbm = LBMCalculator.arrondir(LBMCalculator.calculerLBM(poids, taille, sexe))
        val masseGrasse = LBMCalculator.arrondir(LBMCalculator.calculerMasseGrasse(poids, lbm))
        val pctGrasse = LBMCalculator.arrondir(LBMCalculator.calculerPourcentageMasseGrasse(poids, lbm))
        val pctLBM = LBMCalculator.arrondir(LBMCalculator.calculerPourcentageLBM(poids, lbm))
        val satisfaisant = LBMCalculator.isSatisfaisant(lbm, sexe, seuilHomme, seuilFemme)

        afficherResultats(lbm, poids, pctGrasse, pctLBM, satisfaisant, sexe)

        dernierCalcul = Calcul(
            userId = prefs.getInt("user_id", -1),
            poids = poids,
            taille = taille,
            sexe = sexe,
            lbm = lbm,
            massGrasse = masseGrasse,
            date = SimpleDateFormat("dd MMM yyyy", Locale.FRANCE).format(Date())
        )
    }

    private fun afficherResultats(lbm: Double, poids: Double, pctGrasse: Double, pctLBM: Double, satisfaisant: Boolean, sexe: Sexe) {
        binding.tvLBMValue.text = String.format(Locale.FRANCE, "%.1f", lbm)
        binding.tvPoidsTotal.text = String.format(Locale.FRANCE, "%.1f kg", poids)
        binding.tvMasseGrasse.text = String.format(Locale.FRANCE, "%.1f %%", pctGrasse)

        binding.progressLBM.progress = pctLBM.toInt()
        binding.progressGrasse.progress = pctGrasse.toInt()
        binding.tvProgressLBM.text = String.format(Locale.FRANCE, "%.0f%%", pctLBM)
        binding.tvProgressGrasse.text = String.format(Locale.FRANCE, "%.0f%%", pctGrasse)

        if (satisfaisant) {
            binding.tvResultBadge.text = "✓ Résultat satisfaisant"
            binding.tvResultBadge.setBackgroundResource(R.drawable.bg_badge_success)
            binding.tvResultBadge.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_on_primary))
        } else {
            binding.tvResultBadge.text = "⚠ Résultat à surveiller"
            binding.tvResultBadge.setBackgroundResource(R.drawable.bg_badge_warning)
            binding.tvResultBadge.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_primary))
        }

        binding.tvAnalyse.text = LBMCalculator.analysePerformance(lbm, sexe)
        binding.cardResult.visibility = View.VISIBLE
        binding.btnEnregistrer.visibility = View.VISIBLE
        binding.btnPartager.visibility = View.VISIBLE
    }

    private fun enregistrer() {
        val calcul = dernierCalcul ?: return
        if (calcul.userId == -1) {
            Snackbar.make(binding.root, "Connectez-vous pour enregistrer.", Snackbar.LENGTH_LONG).show()
            return
        }
        val id = db.insertCalcul(calcul)
        if (id != -1L) {
            Snackbar.make(binding.root, "Enregistré dans l'historique !", Snackbar.LENGTH_SHORT).show()
            binding.btnEnregistrer.isEnabled = false
            binding.btnEnregistrer.text = "✓ Enregistré"
        }
    }

    private fun partager() {
        val calcul = dernierCalcul ?: return
        val texte = "📊 Mon résultat LeanMass :\n• Masse maigre (LBM) : ${String.format("%.1f", calcul.lbm)} kg\n• Calculé avec la formule de Boer"
        val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(android.content.Intent.EXTRA_TEXT, texte)
        }
        startActivity(android.content.Intent.createChooser(intent, "Partager mon résultat"))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
