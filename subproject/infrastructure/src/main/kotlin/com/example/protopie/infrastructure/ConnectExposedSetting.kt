package com.example.protopie.infrastructure

import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

object ConnectExposedSetting {

    fun execute(databaseConfiguration: DatabaseConfiguration){
        val hikaricpDatabase: HikariDataSource = HikariDataSource().apply {
            jdbcUrl = databaseConfiguration.jdbcUrl
            username = databaseConfiguration.username
            password = databaseConfiguration.password
        }

        Database.connect(hikaricpDatabase)
    }
}