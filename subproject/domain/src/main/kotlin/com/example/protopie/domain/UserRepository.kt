package com.example.protopie.domain

interface UserRepository {

    fun create(email: String, username: String, password: String, role:User.UserRole):String

    fun findAllCount():Int
    fun findAll(size:Int, offset:Int):List<User>

    fun findByEmail(email: String): User?
    fun findPassword(email: String): User?
    fun findById(id: String): User?
    fun update(user: User):User
}
