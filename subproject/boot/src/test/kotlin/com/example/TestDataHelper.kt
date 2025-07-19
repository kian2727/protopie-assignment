package com.example

import com.example.protopie.domain.PasswordUtil
import com.example.protopie.domain.User
import com.example.protopie.domain.jwt.TokenConfiguration
import com.example.protopie.domain.jwt.TokenProvider
import java.time.LocalDateTime
import java.util.UUID

object TestDataHelper {

    fun generateEmail() = "${UUID.randomUUID()}@example.com"
    fun generateAccessToken(tokenConfiguration:TokenConfiguration, userId:String)=
        TokenProvider(tokenConfiguration).generateToken(userId)

    fun generateRandomUser() = User(
        id = UUID.randomUUID().toString(),
        email = generateEmail(),
        password = PasswordUtil.hashPassword(UUID.randomUUID().toString()),
        username = UUID.randomUUID().toString(),
        role = User.UserRole.entries.random(),
        isActive = true,
        deletedAt = null,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now(),
    )
}