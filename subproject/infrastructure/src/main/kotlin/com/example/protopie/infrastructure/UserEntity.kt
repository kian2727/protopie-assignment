package com.example.protopie.infrastructure

import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object UserEntity: org.jetbrains.exposed.sql.Table("users") {
    val id = uuid("id").clientDefault { java.util.UUID.randomUUID() }
    val email = varchar("email", 255).uniqueIndex()
    val username = varchar("username", 255)
    val password = varchar("password", 255)
    val role = varchar("role", 100)
    val isActive = bool("is_active")
    val deletedAt = datetime("deleted_at").nullable()
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val updatedAt = datetime("updated_at").default(LocalDateTime.now())

    override val primaryKey = PrimaryKey(id)
}