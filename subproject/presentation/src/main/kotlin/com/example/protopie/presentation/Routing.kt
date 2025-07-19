package com.example.protopie.presentation

import com.example.protopie.application.HealthService
import com.example.protopie.application.UserService
import com.example.protopie.domain.User
import com.example.protopie.domain.exception.CustomException
import com.example.protopie.domain.exception.NotFoundUserException
import com.example.protopie.presentation.dto.SignInRequest
import com.example.protopie.presentation.dto.SignInResponse
import com.example.protopie.presentation.dto.SignUpRequest
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.response.*

fun Application.configureRouting(
    healthService: HealthService,
    userService: UserService,
) {
    routing {
        install(ContentNegotiation){
            json()
        }

        fun String.toRoleUser():User.UserRole = when(this){
            "user" -> User.UserRole.USER
            "admin" -> User.UserRole.ADMIN
            else -> throw IllegalArgumentException("Invalid role")
        }

        get("/health"){
            healthService.check()
        }

        post("/signup") {
            try {
                val request = call.receive<SignUpRequest>()
                userService.signup(request.email, request.username, request.password, request.role?.toRoleUser())
                call.respond(HttpStatusCode.NoContent)
            } catch (e: CustomException){
                call.respond(HttpStatusCode.Conflict,"이미 이메일이 존재합니다.[ email =${e.value} ]")
            }
        }

        post("/signin") {
            try {
                val request = call.receive<SignInRequest>()
                val accessToken = userService.signIn(request.email, request.password)
                println("#### access token = $accessToken")
                call.respond(HttpStatusCode.OK, SignInResponse(accessToken))
            } catch (e: NotFoundUserException){
                call.respond(HttpStatusCode.NotFound, "유저를 찾을 수 없습니다.")
            }
        }
    }
}