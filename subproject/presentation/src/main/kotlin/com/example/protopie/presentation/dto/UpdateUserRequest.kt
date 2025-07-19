package com.example.protopie.presentation.dto

import com.example.protopie.application.UserService
import com.example.protopie.domain.User.UserRole

@kotlinx.serialization.Serializable
data class UpdateUserRequest(
    val email:String? = null,
    val password:String? = null,
    val username:String? = null,
    val role: String? = null,
){
    fun toCommand(
        userId:String,
    ) = UserService.UpdateUserCommand(
        userId = userId,
        email = email,
        password = password,
        username = username,
        role = role?.let {
            when(it){
                "ADMIN" -> UserRole.ADMIN
                "USER" -> UserRole.USER
                else -> throw IllegalArgumentException("Invalid role")
            }
         },
    )
}