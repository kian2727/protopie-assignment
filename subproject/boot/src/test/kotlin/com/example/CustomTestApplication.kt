package com.example

import com.example.protopie.infrastructure.DatabaseConfiguration
import com.example.protopie.module
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*

fun runCustomTestApplication(databaseConfiguration: DatabaseConfiguration, block: suspend ApplicationTestBuilder.() -> Unit) {
    testApplication {
        application { module(databaseConfiguration = databaseConfiguration) }
        client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        block()
    }
}