package com.example

import com.example.protopie.domain.jwt.TokenConfiguration
import com.example.protopie.domain.jwt.TokenProvider
import java.util.UUID

object TestDataHelper {

    fun generateEmail() = "${UUID.randomUUID()}@example.com"
    fun generateAccessToken(tokenConfiguration:TokenConfiguration, userId:String)=
        TokenProvider(tokenConfiguration).generateToken(userId)
}