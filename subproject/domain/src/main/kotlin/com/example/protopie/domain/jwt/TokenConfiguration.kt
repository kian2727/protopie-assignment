package com.example.protopie.domain.jwt

import com.typesafe.config.Config

data class TokenConfiguration(
    val issuer: String,
    val audience: String,
    val secret: String,
    val expiryPeriod: Long = 600_000 // 10분
){
    companion object{
        fun loadConfiguration(config: Config): TokenConfiguration =
            TokenConfiguration(
                issuer = config.getString("jwt.issuer"),
                audience = config.getString("jwt.audience"),
                secret = config.getString("jwt.secret"),
                expiryPeriod = config.getLong("jwt.expiryPeriod"),
            )
        }
}
