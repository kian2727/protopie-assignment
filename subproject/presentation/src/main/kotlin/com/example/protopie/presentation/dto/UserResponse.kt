package com.example.protopie.presentation.dto

import com.example.protopie.domain.User
import com.example.protopie.domain.User.UserRole
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id:String,
    val email:String,
    val password:String,
    val username:String,
    val role: UserRole,
    val isActive:Boolean,
    val deletedAt: String?,
    val createdAt: String,
    val updatedAt: String
){
    companion object{
        fun from(user:User) = UserResponse(
            id = user.id,
            email = user.email,
            password = user.password,
            username = user.username,
            role = user.role,
            isActive = user.isActive,
            deletedAt = user.deletedAt?.toString(),
            createdAt = user.createdAt.toString(),
            updatedAt = user.updatedAt.toString(),
        )
    }

}
