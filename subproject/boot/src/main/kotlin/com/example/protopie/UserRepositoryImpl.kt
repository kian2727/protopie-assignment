package com.example.protopie

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

class UserRepositoryImpl(private val connection:Database): UserRepository {

    init {
        transaction(connection) {
            SchemaUtils.create(Users)
        }
    }

    override fun create(email: String, username: String, password: String):String {
        val test = transaction {
            Users.insert {
                it[Users.email] = email
                it[Users.username] = username
                it[Users.password] = password
            }[Users.id]
        }

        return test.toString()
    }
}