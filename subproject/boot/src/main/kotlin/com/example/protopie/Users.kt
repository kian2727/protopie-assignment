package com.example.protopie

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object Users: Table("users") {
    val id = uuid("id").clientDefault { java.util.UUID.randomUUID() }
    val email = varchar("email", 255)
    val username = varchar("username", 255)
    val password = varchar("password", 255)
    val deletedAt = datetime("deleted_at").nullable()
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")

    override val primaryKey = PrimaryKey(id)
}