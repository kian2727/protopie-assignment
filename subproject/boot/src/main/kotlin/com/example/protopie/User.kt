package com.example.protopie

import java.time.LocalDateTime

data class User(
    val id:String,
    val email:String,
    val username:String,
    val deletedAt:LocalDateTime?,
    val createdAt:LocalDateTime,
    val updatedAt:LocalDateTime,
)