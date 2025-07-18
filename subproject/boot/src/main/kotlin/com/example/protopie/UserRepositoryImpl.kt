package com.example.protopie

class UserRepositoryImpl(): UserRepository {
    override fun create(email: String, username: String, password: String):String {
        return "uuid"
    }
}
