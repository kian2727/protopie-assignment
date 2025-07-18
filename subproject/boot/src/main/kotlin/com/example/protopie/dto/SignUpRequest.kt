package com.example.protopie.dto
import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    val email:String,
    val username: String,
    val password: String
)
