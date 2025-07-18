package com.example.protopie

interface UserRepository {

    fun create(email: String, username: String, password: String):String
}
