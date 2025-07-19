package com.example.protopie.domain

import java.time.LocalDateTime

data class User(
    val id:String,
    val email:String,
    val password:String,
    val username:String,
    val role:UserRole,
    val isActive:Boolean,
    val deletedAt: LocalDateTime?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
){

    enum class UserRole{
        ADMIN,
        USER
    }
}