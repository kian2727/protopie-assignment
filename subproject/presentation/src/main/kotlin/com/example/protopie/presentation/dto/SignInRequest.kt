package com.example.protopie.presentation.dto

import kotlinx.serialization.Serializable

@Serializable
data class SignInRequest(
    val email:String,
    val password: String
)
