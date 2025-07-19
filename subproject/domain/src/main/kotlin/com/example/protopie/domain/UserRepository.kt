package com.example.protopie.domain

interface UserRepository {

    fun create(email: String, username: String, password: String, role:User.UserRole):String
    fun findByEmail(email: String): User?
    fun findPassword(email: String): User?
    fun update(user: User):User?
}
