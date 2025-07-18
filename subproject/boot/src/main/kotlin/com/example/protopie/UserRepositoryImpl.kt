package com.example.protopie

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

class UserRepositoryImpl: UserRepository {

    override fun create(email: String, username: String, password: String):String {
        val test = transaction {
            UserEntity.insert {
                it[UserEntity.email] = email
                it[UserEntity.username] = username
                it[UserEntity.password] = password
            }[UserEntity.id]
        }

        return test.toString()
    }
}