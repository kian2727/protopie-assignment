package com.example.protopie.infrastructure

import com.example.protopie.domain.User
import com.example.protopie.domain.UserRepository
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class UserRepositoryImpl: UserRepository {

    override fun create(email: String, username: String, password: String):String {
        val userId = org.jetbrains.exposed.sql.transactions.transaction {
            UserEntity.insert {
                it[UserEntity.email] = email
                it[UserEntity.username] = username
                it[UserEntity.password] = password
            }[UserEntity.id].toString()
        }

        return userId
    }

    override fun findByEmail(email: String): User? = org.jetbrains.exposed.sql.transactions.transaction {
        UserEntity.select {
            UserEntity.email eq email
        }.map {
            User(
                id = it[UserEntity.id].toString(),
                email = it[UserEntity.email].toString(),
                username = it[UserEntity.username].toString(),
                deletedAt = it[UserEntity.deletedAt],
                createdAt = it[UserEntity.createdAt],
                updatedAt = it[UserEntity.updatedAt],
            )
        }.singleOrNull()
    }
}