package com.example.protopie.presentation.dto

import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    val email:String,
    val username: String,
    val password: String,
    val role:String? = null
)
