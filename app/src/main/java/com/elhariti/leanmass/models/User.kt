package com.elhariti.leanmass.models

data class User(
    val id: Int = 0,
    val name: String,
    val email: String,
    val passwordHash: String
)