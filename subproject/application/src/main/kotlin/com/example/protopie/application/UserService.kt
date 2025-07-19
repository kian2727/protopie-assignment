package com.example.protopie.application

import com.example.protopie.domain.User

interface UserService {

    fun signup(email: String, username: String, password: String, role: User.UserRole?)
    fun signIn(email: String, password: String):String
    fun delete(userId: String)
}