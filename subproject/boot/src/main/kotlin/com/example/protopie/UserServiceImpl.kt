package com.example.protopie

class UserServiceImpl(
    private val usersRepository:UserRepository
):UserService {

    override fun signup(email: String, username: String, password: String) {
        // TODO email validation check
        usersRepository.create(
            email,
            username,
            PasswordUtil.hashPassword(password)
        )
    }
}