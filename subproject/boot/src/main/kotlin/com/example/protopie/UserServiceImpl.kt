package com.example.protopie

class UserServiceImpl(
    private val usersRepository:UserRepository
):UserService {

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