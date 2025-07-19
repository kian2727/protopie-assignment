package com.example.protopie.domain.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

class TokenProvider(private val config: TokenConfiguration) {
    fun generateToken(userId: String): String {
        val now = System.currentTimeMillis()
        val expiry = now + config.expiryPeriod

                return JWT.create()
            .withIssuer(config.issuer)
            .withAudience(config.audience)
            .withClaim("userId", userId)
            .withExpiresAt(Date(expiry))
            .sign(Algorithm.HMAC256(config.secret))
    }
}