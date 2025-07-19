package com.example.protopie.presentation.dto

import com.example.protopie.application.UserService
import kotlinx.serialization.Serializable

@Serializable
data class UsersResponse(
    @Serializable
    val elements: List<UserResponse>,
    val page: Int,
    val size: Int,
    val totalCount:Int,
){
    companion object{
        fun from(result:UserService.GetUsersAllResult) = UsersResponse(
            elements = result.content.map { UserResponse.from(it) },
            page = result.page,
            size = result.size,
            totalCount = result.totalCount
        )
    }
}
