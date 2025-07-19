package com.example.protopie.infrastructure

import com.example.protopie.domain.User
import com.example.protopie.domain.UserRepository
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.util.*

class UserRepositoryImpl: UserRepository {

    override fun create(email: String, username: String, password: String, role: User.UserRole):String {
        val userId = transaction {
            UserEntity.insert {
                it[UserEntity.email] = email
                it[UserEntity.username] = username
                it[UserEntity.password] = password
                it[UserEntity.role] = role.toString()
            }[UserEntity.id].toString()
        }

        return userId
    }

    override fun findByEmail(email: String): User? = transaction {
        UserEntity.select {
            UserEntity.email eq email
        }.toUserDomain().singleOrNull()
    }

    override fun findPassword(email: String): User? = transaction{
        UserEntity.select {
            UserEntity.email eq email
        }.toUserDomain().singleOrNull()
    }

    override fun findById(id: String): User? = transaction {
        UserEntity.select(
            UserEntity.id eq UUID.fromString(id)
        ).toUserDomain().singleOrNull()
    }

    override fun update(user: User): User = transaction{
        val userIdUUid = UUID.fromString(user.id)
        UserEntity.update({ UserEntity.id eq userIdUUid }) {
            it[password] = user.password
            it[email] = user.email
            it[username] = user.username
            it[role] = user.role.toString()
            it[isActive] = user.isActive
            it[deletedAt] = user.deletedAt
            it[updatedAt] = user.updatedAt
        }

        UserEntity.select(UserEntity.id eq userIdUUid ).toUserDomain().single()
    }

    private fun Query.toUserDomain()=this.map {
        User(
            id = it[UserEntity.id].toString(),
            password = it[UserEntity.password].toString(),
            email = it[UserEntity.email].toString(),
            username = it[UserEntity.username].toString(),
            role = it[UserEntity.role].toRole(),
            isActive = it[UserEntity.isActive],
            deletedAt = it[UserEntity.deletedAt],
            createdAt = it[UserEntity.createdAt],
            updatedAt = it[UserEntity.updatedAt],
        )
    }

    private fun String.toRole():User.UserRole =
        when(this){
            "ADMIN" -> User.UserRole.ADMIN
            "USER" -> User.UserRole.USER
            else -> throw RuntimeException("failed to convert role. [ value = $this] ")
        }
}