package com.example.protopie

import de.mkammerer.argon2.Argon2Factory

object PasswordUtil {
    private val argon2 = Argon2Factory.create()

    fun hashPassword(password: String): String {
        return argon2.hash(
            2,      // iterations
            65536,  // memory in KB (64 MB)
            1,      // parallelism
            password.toCharArray()
        )
    }

    fun verifyPassword(hash: String, password: String): Boolean {
        return argon2.verify(hash, password.toCharArray())
    }
}
