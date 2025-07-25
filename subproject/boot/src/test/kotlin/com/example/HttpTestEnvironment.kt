package com.example

import com.example.protopie.domain.jwt.TokenConfiguration
import com.example.protopie.infrastructure.DatabaseConfiguration
import com.typesafe.config.ConfigFactory
import org.testcontainers.containers.PostgreSQLContainer

class HttpTestEnvironment : AutoCloseable {

    private val postgreSQLContainer:PostgreSQLContainer<*>
    val databaseConfiguration: DatabaseConfiguration
    val tokenConfiguration:TokenConfiguration

    companion object{
        private const val INIT_SCRIPT = "create-tables.sql"
        val global = HttpTestEnvironment()
    }

    init{
        val config = ConfigFactory.load()

        val originalPort = config.getInt("database.postgresql.port")
        postgreSQLContainer = PostgreSQLContainer("postgres:latest")
            .withDatabaseName(config.getString("database.postgresql.database"))
            .withUsername(config.getString("database.postgresql.username"))
            .withPassword(config.getString("database.postgresql.password"))
            .withInitScript(INIT_SCRIPT)

        if( postgreSQLContainer.isRunning.not() ) {
            postgreSQLContainer.start()
        }

        val exposedPort = postgreSQLContainer.getMappedPort(originalPort)
        databaseConfiguration = DatabaseConfiguration.loadConfiguration(config).copy(port = exposedPort)
        tokenConfiguration = TokenConfiguration.loadConfiguration(config)

    }

    override fun close() {
        postgreSQLContainer.close()
    }
}