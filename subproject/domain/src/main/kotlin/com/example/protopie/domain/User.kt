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

    fun toDelete() = this.copy(
        deletedAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now(),
        isActive = false)

    fun toUpdate(
        email:String?,
        password:String?,
        username:String?,
        role:UserRole?,
    ) = this.copy(
        email = email?:this.email ,
        password = password?:this.password,
        username = username?:this.username,
        role = role?:this.role,
    )
}