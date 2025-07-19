package com.example.protopie.application

import com.example.protopie.domain.User

interface UserService {

    fun signup(email: String, username: String, password: String, role: User.UserRole?)
    fun signIn(email: String, password: String):String
    fun getUsersAll(command:GetUsersAllCommand):GetUsersAllResult

    data class GetUsersAllCommand(val userId:String, val page:Int, val size:Int)
    data class GetUsersAllResult(
        val page:Int,
        val size:Int,
        val totalCount:Int,
        val content:List<User>
    )

    fun updateUser(command:UpdateUserCommand):User
    fun getUser(userId:String): User
    fun delete(userId: String)

    data class UpdateUserCommand(
        val userId:String,
        val email:String?,
        val username:String?,
        val password:String?,
        val role: User.UserRole?
    )

}