package com.example.protopie.application

import com.example.protopie.domain.PasswordUtil
import com.example.protopie.domain.User
import com.example.protopie.domain.jwt.TokenProvider
import com.example.protopie.domain.UserRepository
import com.example.protopie.domain.exception.AuthorizationFailedException
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

    override fun getUsersAll(command: UserService.GetUsersAllCommand): UserService.GetUsersAllResult {
        val user = usersRepository.findById(command.userId) ?: throw NotFoundUserException()
        if( user.role != User.UserRole.ADMIN ){
            throw AuthorizationFailedException()
        }

        val totalCount = usersRepository.findAllCount()
        val size = command.size
        val offset = (command.page - 1) * size
        val usersList = usersRepository.findAll(size, offset)

        return UserService.GetUsersAllResult(command.page, size, totalCount, usersList)
    }

    override fun updateUser(command: UserService.UpdateUserCommand):User {
        val user =  usersRepository.findById(command.userId) ?: throw NotFoundUserException()
        val toUpdateUser = user.toUpdate(command.email, command.password, command.username, command.role)
        return usersRepository.update(toUpdateUser)
    }

    override fun getUser(userId: String): User = usersRepository.findById(userId) ?: throw NotFoundUserException()

    override fun delete(userId: String) {
        val user = usersRepository.findById(userId) ?: throw NotFoundUserException()
        val userToDelete = user.toDelete()
        usersRepository.update(userToDelete)
    }
}