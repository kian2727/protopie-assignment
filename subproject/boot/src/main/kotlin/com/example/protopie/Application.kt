package com.example.protopie

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database

fun main(args: Array<String> = emptyArray()) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module(databaseConfiguration:DatabaseConfiguration? = null) {

    val config: Config = ConfigFactory.load()
    val databaseConfiguration = databaseConfiguration?:DatabaseConfiguration.loadConfiguration(config)

    val hikaricpDatabase: HikariDataSource = HikariDataSource().apply {
        jdbcUrl = databaseConfiguration.jdbcUrl
        username = databaseConfiguration.username
        password = databaseConfiguration.password
    }

    Database.connect(hikaricpDatabase)

    val userRepository = UserRepositoryImpl()
    val userService = UserServiceImpl(userRepository)

    configureRouting(userService)
}