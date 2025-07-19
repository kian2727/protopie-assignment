package com.example.protopie.domain

interface UserRepository {

    fun create(email: String, username: String, password: String):String
    fun findByEmail(email: String): User?
    fun findPassword(email: String): Pair<User,String>?
}
