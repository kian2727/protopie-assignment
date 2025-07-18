package com.example.protopie

import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import com.typesafe.config.Config

fun configureDatabase(configuration:Config) {

    val hikaricpDatabase: HikariDataSource = HikariDataSource().apply {
        jdbcUrl = configuration.getString("database.postgresql.jdbcUrl")
        username = "postgres"
        password = "postgres"
    }

    Database.connect(hikaricpDatabase)
}