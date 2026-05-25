package com.elhariti.leanmass.models

data class Calcul(
    val id: Int = 0,
    val userId: Int,
    val poids: Double,
    val taille: Int,
    val sexe: Sexe,
    val lbm: Double,
    val massGrasse: Double,
    val date: String
)

enum class Sexe {
    HOMME, FEMME
}
