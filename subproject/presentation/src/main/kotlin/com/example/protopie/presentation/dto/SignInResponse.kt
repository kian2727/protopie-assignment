package com.example.protopie.presentation.dto

import kotlinx.serialization.Serializable

@Serializable
data class SignInResponse(
    val accessToken: String
)
