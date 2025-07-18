package com.example.protopie

import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val userRepository = UserRepositoryImpl()
    val userService = UserServiceImpl(userRepository)
    configureRouting(userService)
}