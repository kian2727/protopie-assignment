package com.example.protopie.application

import com.example.protopie.domain.PasswordUtil
import com.example.protopie.domain.User
import com.example.protopie.domain.jwt.TokenProvider
import com.example.protopie.domain.UserRepository
import com.example.protopie.domain.exception.CustomException
import com.example.protopie.domain.exception.NotFoundUserException

class UserServiceImpl(
    private val usersRepository: UserRepository,
    private val tokenProvider: TokenProvider
): UserService {

    override fun signup(email: String, username: String, password: String, role: User.UserRole?) {
        if (usersRepository.findByEmail(email) != null) {
            throw CustomException(email)
        }

        usersRepository.create(
            email = email,
            username = username,
            password = PasswordUtil.hashPassword(password),
            role = role?: User.UserRole.USER
        )
    }

    override fun signIn(email: String, password: String): String {
        val user= usersRepository.findPassword(email) ?: throw NotFoundUserException()
        if( PasswordUtil.verifyPassword(user.password, password) && user.isActive){
            return tokenProvider.generateToken(user.id)
        } else {
            throw NotFoundUserException()
        }
    }
}