package com.example.protopie.application

import com.example.protopie.domain.PasswordUtil
import com.example.protopie.domain.UserRepository
import com.example.protopie.domain.exception.AlreadyExistedException

class UserServiceImpl(
    private val usersRepository: UserRepository
): UserService {

    override fun signup(email: String, username: String, password: String) {
        if (usersRepository.findByEmail(email) != null) {
            throw AlreadyExistedException(email)
        }

        usersRepository.create(
            email,
            username,
            PasswordUtil.hashPassword(password)
        )
    }
}