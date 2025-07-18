package com.example.protopie

import com.typesafe.config.ConfigFactory
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    val config = ConfigFactory.load()
    configureDatabase(config)

    val userRepository = UserRepositoryImpl()
    val userService = UserServiceImpl(userRepository)

    configureRouting(userService)
}