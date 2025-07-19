package com.example.protopie.presentation

import com.example.protopie.application.UserService
import com.example.protopie.domain.exception.AlreadyExistedException
import com.example.protopie.presentation.dto.SignUpRequest
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.response.*

fun Application.configureRouting(userService: UserService) {
    routing {

        install(ContentNegotiation){
            json()
        }

        post("/signup") {
            try {
                val request = call.receive<SignUpRequest>()
                userService.signup(request.email, request.username, request.password)
                call.respond(HttpStatusCode.NoContent)
            } catch (e: AlreadyExistedException){
                call.respond(HttpStatusCode.Conflict,"이미 이메일이 존재합니다.[ email =${e.value} ]")
            }

        }
    }
}