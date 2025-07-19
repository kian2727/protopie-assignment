package com.example.protopie

import com.example.protopie.application.HealthServiceImpl
import com.example.protopie.application.UserServiceImpl
import com.example.protopie.domain.jwt.TokenConfiguration
import com.example.protopie.domain.jwt.TokenProvider
import com.example.protopie.infrastructure.ConnectExposedSetting
import com.example.protopie.infrastructure.DatabaseConfiguration
import com.example.protopie.infrastructure.HealthRepositoryImpl
import com.example.protopie.infrastructure.UserRepositoryImpl
import com.example.protopie.presentation.configureRouting
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import io.ktor.server.application.*

fun main(args: Array<String> = emptyArray()) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module(databaseConfiguration: DatabaseConfiguration? = null) {

    val config: Config = ConfigFactory.load()
    ConnectExposedSetting.execute(databaseConfiguration?: DatabaseConfiguration.loadConfiguration(config))

    val healthRepository = HealthRepositoryImpl()
    val healthService = HealthServiceImpl(healthRepository)

    val userRepository = UserRepositoryImpl()
    val tokenProvider = TokenProvider(TokenConfiguration.loadConfiguration(config))

    val userService = UserServiceImpl(userRepository, tokenProvider)

    configureRouting(healthService, userService)
}