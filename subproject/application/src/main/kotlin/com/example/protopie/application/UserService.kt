package com.example.protopie.application

interface UserService {

    fun signup(email:String, username: String, password: String)
    fun signIn(email: String, password: String):String
}