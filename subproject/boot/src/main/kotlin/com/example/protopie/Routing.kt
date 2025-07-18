package com.example.protopie

import com.example.protopie.dto.SignUpRequest
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.response.*

fun Application.configureRouting(userService:UserService) {
    routing {

        install(ContentNegotiation){
            json()
        }

        post("/signup") {
            val request = call.receive<SignUpRequest>()
            userService.signup(request.email, request.username, request.password)

            call.respond(HttpStatusCode.NoContent)
        }
    }
}