import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.protopie.domain.jwt.TokenConfiguration
import com.example.protopie.presentation.dto.ErrorResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun Application.configAuth(config: TokenConfiguration){
    install(Authentication) {
        jwt("jwtAuth") {
            realm = config.realm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(config.secret))
                    .withAudience(config.audience)
                    .withIssuer(config.issuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.getClaim("userId").asString().isNotEmpty()) {
                    JWTPrincipal(credential.payload)
                } else null
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, ErrorResponse("토큰이 유효하지 않거나 만료되었습니다."))
            }
        }
    }
}