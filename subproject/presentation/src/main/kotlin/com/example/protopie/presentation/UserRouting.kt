package com.example.protopie.presentation

import com.example.protopie.application.UserService
import com.example.protopie.presentation.dto.UserResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.authRouting(userService: UserService){
    routing {
        authenticate("jwtAuth") {
            get("/users/{userId}") {
                val userId = call.request.pathVariables["userId"] ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    "userId 를 찾을수 없습니다."
                )
                val principal = call.principal<JWTPrincipal>()
                val principalUserId = principal?.payload?.getClaim("userId")?.asString()
                if (userId == principalUserId) {
                    val user = userService.getUser(userId)
                    call.respond(HttpStatusCode.OK, UserResponse.from(user))
                } else {
                    call.respond(HttpStatusCode.Forbidden, "You can't delete this user")
                }
            }
            delete("/users/{userId}") {
                val userId = call.request.pathVariables["userId"] ?: return@delete call.respond(
                    HttpStatusCode.BadRequest,
                    "userId 를 찾을수 없습니다."
                )
                val principal = call.principal<JWTPrincipal>()
                val principalUserId = principal?.payload?.getClaim("userId")?.asString()
                if (userId == principalUserId) {
                    userService.delete(userId)
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.Forbidden, "You can't delete this user")
                }
            }
        }
    }
}