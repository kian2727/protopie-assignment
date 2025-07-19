package com.example.protopie.application

import com.example.protopie.domain.PasswordUtil
import com.example.protopie.domain.UserRepository
import com.example.protopie.domain.exception.CustomException
import com.example.protopie.domain.exception.NotFoundUserException

class UserServiceImpl(
    private val usersRepository: UserRepository
): UserService {

    override fun signup(email: String, username: String, password: String) {
        if (usersRepository.findByEmail(email) != null) {
            throw CustomException(email)
        }

        usersRepository.create(
            email,
            username,
            PasswordUtil.hashPassword(password)
        )
    }

    override fun signIn(email: String, password: String): String {

        val hashedPassword = usersRepository.findPassword(email) ?: throw NotFoundUserException()
        if( PasswordUtil.verifyPassword(hashedPassword, password) ){
            return "TODO accessToken"
        } else {
            throw NotFoundUserException()
        }
    }
}