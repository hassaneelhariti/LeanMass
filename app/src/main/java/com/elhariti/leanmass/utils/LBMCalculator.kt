package com.elhariti.leanmass.utils


import com.elhariti.leanmass.models.Sexe
import kotlin.math.roundToInt

object LBMCalculator {

    // Formules de Boer
    fun calculerLBM(poids: Double, taille: Int, sexe: Sexe): Double {
        return when (sexe) {
            Sexe.HOMME -> (0.407 * poids) + (0.267 * taille) - 19.2
            Sexe.FEMME -> (0.252 * poids) + (0.473 * taille) - 48.3
        }
    }

    fun calculerMasseGrasse(poids: Double, lbm: Double): Double {
        return poids - lbm
    }

    fun calculerPourcentageMasseGrasse(poids: Double, lbm: Double): Double {
        val masseGrasse = calculerMasseGrasse(poids, lbm)
        return (masseGrasse / poids) * 100
    }

    fun calculerPourcentageLBM(poids: Double, lbm: Double): Double {
        return (lbm / poids) * 100
    }

    fun isSatisfaisant(lbm: Double, sexe: Sexe, seuilHomme: Double = 38.0, seuilFemme: Double = 24.0): Boolean {
        return when (sexe) {
            Sexe.HOMME -> lbm >= seuilHomme
            Sexe.FEMME -> lbm >= seuilFemme
        }
    }

    fun analysePerformance(lbm: Double, sexe: Sexe): String {
        return if (isSatisfaisant(lbm, sexe)) {
            "Votre masse maigre est dans la zone cible pour votre profil athlétique. " +
                    "Cela indique une excellente rétention musculaire par rapport à votre pourcentage de graisse."
        } else {
            "Votre masse maigre est en dessous de la zone cible. " +
                    "Envisagez d'ajuster votre alimentation et votre programme d'entraînement pour améliorer votre composition corporelle."
        }
    }

    fun arrondir(valeur: Double, decimales: Int = 1): Double {
        val facteur = Math.pow(10.0, decimales.toDouble())
        return (valeur * facteur).roundToInt() / facteur
    }
}