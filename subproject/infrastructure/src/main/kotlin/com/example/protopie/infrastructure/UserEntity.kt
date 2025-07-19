package com.example.protopie.infrastructure

import org.jetbrains.exposed.sql.javatime.datetime

object UserEntity: org.jetbrains.exposed.sql.Table("users") {
    val id = uuid("id").clientDefault { java.util.UUID.randomUUID() }
    val email = varchar("email", 255).uniqueIndex()
    val username = varchar("username", 255)
    val password = varchar("password", 255)
    val role = varchar("role", 100)
    val isActive = bool("is_active")
    val deletedAt = datetime("deleted_at").nullable()
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")

    override val primaryKey = PrimaryKey(id)
}