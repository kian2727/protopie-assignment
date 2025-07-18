package com.example.protopie

import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    val hikaricpDatabase: HikariDataSource = HikariDataSource().apply {
        jdbcUrl = "jdbc:postgresql://localhost:5432/postgres"
        username = "postgres"
        password = "postgres"
    }

    val connection = Database.connect(hikaricpDatabase)

    val userRepository = UserRepositoryImpl(connection)
    val userService = UserServiceImpl(userRepository)
    configureRouting(userService)
}