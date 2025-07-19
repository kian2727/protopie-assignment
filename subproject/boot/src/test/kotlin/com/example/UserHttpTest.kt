package com.example

import com.example.TestDataHelper.generateAccessToken
import com.example.TestDataHelper.generateEmail
import com.example.TestDataHelper.generateRandomUser
import com.example.protopie.domain.PasswordUtil
import com.example.protopie.domain.jwt.TokenConfiguration
import com.example.protopie.infrastructure.DatabaseConfiguration
import com.example.protopie.infrastructure.UserEntity
import com.example.protopie.presentation.dto.UpdateUserRequest
import com.example.protopie.presentation.dto.UserResponse
import com.example.protopie.presentation.dto.UsersResponse
import io.kotest.core.spec.style.FreeSpec
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import junit.framework.TestCase.assertEquals
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import java.util.Random

class UserHttpTest:FreeSpec({
    lateinit var databaseConfiguration: DatabaseConfiguration
    lateinit var tokenConfiguration: TokenConfiguration
    beforeTest {
        databaseConfiguration = HttpTestEnvironment.global.databaseConfiguration
        tokenConfiguration = HttpTestEnvironment.global.tokenConfiguration
    }

    "유저 관리 테스트" - {
        "인증 실패 테스트" - {
            "token이 없는경우"{
                runCustomTestApplication(databaseConfiguration) {
                    val response = client.delete("/users/non-user")
                    assertEquals(HttpStatusCode.Unauthorized, response.status)
                }
            }
            "token이 만료되어있는경우" {
                runCustomTestApplication(databaseConfiguration) {
                    val accessToken = generateAccessToken(tokenConfiguration.copy(expiryPeriod = -1000), "userId")
                    val response = client.delete("/users/non-user"){
                        header("Authorization", "Bearer $accessToken")
                    }
                    assertEquals(HttpStatusCode.Unauthorized, response.status)
                }
            }
        }
        "유저 조회 테스트" - {
            "성공 케이스" {
                runCustomTestApplication(databaseConfiguration) {

                    val existedEmail = generateEmail()
                    val existedPassword = UUID.randomUUID().toString()

                    val existedUserId = transaction{
                        UserEntity.insert {
                            it[email] = existedEmail
                            it[username] = "testUsername"
                            it[password] = PasswordUtil.hashPassword(existedPassword)
                            it[role] = "USER"
                        }

                        UserEntity.select(
                            UserEntity.email eq existedEmail
                        ).map { it[UserEntity.id].toString() }.single()
                    }

                    val accessToken = generateAccessToken(tokenConfiguration, existedUserId)

                    val getResponse = client.get("/users/$existedUserId") {
                        header("Authorization", "Bearer $accessToken")
                    }
                    assertEquals(HttpStatusCode.OK, getResponse.status)
                    assertEquals(existedUserId, getResponse.body<UserResponse>().id)

                }
            }
            "실패 케이스" - {
                "로그인한 유저와 path parameter UserId가 다른경우"{
                    runCustomTestApplication(databaseConfiguration) {

                        val existedEmail = generateEmail()
                        val existedPassword = UUID.randomUUID().toString()

                        val existedUserId = transaction{
                            UserEntity.insert {
                                it[email] = existedEmail
                                it[username] = "testUsername"
                                it[password] = PasswordUtil.hashPassword(existedPassword)
                                it[role] = "USER"
                            }

                            UserEntity.select(
                                UserEntity.email eq existedEmail
                            ).map { it[UserEntity.id].toString() }.single()
                        }

                        val accessToken = generateAccessToken(tokenConfiguration, existedUserId)

                        val deleteResponse = client.get("/users/non-userId") {
                            header("Authorization", "Bearer $accessToken")
                        }

                        assertEquals(HttpStatusCode.Forbidden, deleteResponse.status)

                    }
                }
            }
        }
        "유저 전체 조회 테스트" - {
            "성공 케이스" - {
                // ADMIN 유저의 token 생성
                var existedUserId = ""
                val generateUserCount = Random().nextInt(30,100) + 1
                val existedUserTotalCount = generateUserCount + 1 // 유저 하나를 생성했기 때문에

                runCustomTestApplication(databaseConfiguration) {

                    val existedEmail = generateEmail()
                    val existedPassword = UUID.randomUUID().toString()


                    existedUserId = transaction{
                        // 유저가 남아있을 수 있어서 삭제 한다.
                        UserEntity.deleteAll()

                        UserEntity.insert {
                            it[email] = existedEmail
                            it[username] = "testUsername"
                            it[password] = PasswordUtil.hashPassword(existedPassword)
                            it[role] = "ADMIN"
                        }

                        UserEntity.select(
                            UserEntity.email eq existedEmail
                        ).map { it[UserEntity.id].toString() }.single()
                    }

                    // 랜덤으로 유저 생성
                    transaction {

                        val users = (0 until generateUserCount).map {
                            generateRandomUser()
                        }

                        UserEntity.batchInsert(users){ user ->
                            this[UserEntity.email] = user.email
                            this[UserEntity.username] = user.username
                            this[UserEntity.password] = user.password
                            this[UserEntity.role] = user.role.toString()
                            this[UserEntity.isActive] = user.isActive
                        }
                    }
                }

                val adminAccessToken = generateAccessToken(tokenConfiguration, existedUserId)
                "Query parameter 가 없는경우" {
                    runCustomTestApplication(databaseConfiguration) {
                        val getResponse = client.get("/users") {
                            header("Authorization", "Bearer $adminAccessToken")
                        }

                        assertEquals(HttpStatusCode.OK, getResponse.status)
                        assertEquals(getResponse.body<UsersResponse>().totalCount, existedUserTotalCount)
                        assertEquals(getResponse.body<UsersResponse>().size, 10)
                        assertEquals(getResponse.body<UsersResponse>().page, 1)
                    }
                }
                "두번째 페이지" {
                    runCustomTestApplication(databaseConfiguration) {
                        val getResponse = client.get("/users") {
                            header("Authorization", "Bearer $adminAccessToken")
                            parameter("page", 2)
                            parameter("size", 5)
                        }

                        assertEquals(HttpStatusCode.OK, getResponse.status)
                        assertEquals(getResponse.body<UsersResponse>().totalCount, existedUserTotalCount)
                        assertEquals(getResponse.body<UsersResponse>().page, 2)
                        assertEquals(getResponse.body<UsersResponse>().elements.size, 5)
                    }
                }
            }

        }
        "유저 수정 테스트" - {
            var existedUserId = ""
            runCustomTestApplication(databaseConfiguration) {

                val existedEmail = generateEmail()
                val existedPassword = UUID.randomUUID().toString()

                existedUserId = transaction{
                    UserEntity.insert {
                        it[email] = existedEmail
                        it[username] = "testUsername"
                        it[password] = PasswordUtil.hashPassword(existedPassword)
                        it[role] = "USER"
                    }

                    UserEntity.select(
                        UserEntity.email eq existedEmail
                    ).map { it[UserEntity.id].toString() }.single()
                }
            }

            val accessToken = generateAccessToken(tokenConfiguration, existedUserId)

            "성공 케이스" - {
                "전체 수정" {
                    runCustomTestApplication(databaseConfiguration) {
                        val toUpdateEmail = generateEmail()
                        val toUpdateUsername = UUID.randomUUID().toString()

                        val updatedResponse = client.put("/users/$existedUserId") {
                            header("Authorization", "Bearer $accessToken")
                            contentType(ContentType.Application.Json)
                            setBody(
                                UpdateUserRequest(
                                    email = toUpdateEmail,
                                    username = toUpdateUsername,
                                    role = "USER"
                                )
                            )
                        }

                        assertEquals(HttpStatusCode.OK, updatedResponse.status)
                        assertEquals(toUpdateEmail , updatedResponse.body<UserResponse>().email)
                        assertEquals(toUpdateUsername, updatedResponse.body<UserResponse>().username)
                    }
                }
                "일부(username) 수정" {
                    runCustomTestApplication(databaseConfiguration) {
                        val toUpdateUsername = UUID.randomUUID().toString()

                        val updatedResponse = client.put("/users/$existedUserId") {
                            header("Authorization", "Bearer $accessToken")
                            contentType(ContentType.Application.Json)
                            setBody(
                                UpdateUserRequest(
                                    username = toUpdateUsername,
                                )
                            )
                        }

                        assertEquals(HttpStatusCode.OK, updatedResponse.status)
                        assertEquals(toUpdateUsername, updatedResponse.body<UserResponse>().username)
                    }
                }
            }
            "실패 케이스" - {
                "잘못된 유저 Role을 요청한 경우"{
                    runCustomTestApplication(databaseConfiguration) {
                        val updatedResponse = client.put("/users/$existedUserId") {
                            header("Authorization", "Bearer $accessToken")
                            contentType(ContentType.Application.Json)
                            setBody(
                                UpdateUserRequest(
                                    role = "non-existed-role",
                                )
                            )
                        }

                        assertEquals(HttpStatusCode.BadRequest, updatedResponse.status)
                    }
                }
            }
        }
        "유저 삭제 테스트" - {
            "성공 케이스" {
                runCustomTestApplication(databaseConfiguration) {

                    val existedEmail = generateEmail()
                    val existedPassword = UUID.randomUUID().toString()

                    val existedUserId = transaction{
                        UserEntity.insert {
                            it[email] = existedEmail
                            it[username] = "testUsername"
                            it[password] = PasswordUtil.hashPassword(existedPassword)
                            it[role] = "USER"
                        }

                        UserEntity.select(
                            UserEntity.email eq existedEmail
                        ).map { it[UserEntity.id].toString() }.single()
                    }

                    val accessToken = generateAccessToken(tokenConfiguration, existedUserId)

                    val deleteResponse = client.delete("/users/$existedUserId") {
                        header("Authorization", "Bearer $accessToken")
                    }

                    assertEquals(HttpStatusCode.NoContent, deleteResponse.status)

                }
            }
            "실패 케이스" - {
                "로그인한 유저와 path parameter UserId가 다른경우"{
                    runCustomTestApplication(databaseConfiguration) {

                        val existedEmail = generateEmail()
                        val existedPassword = UUID.randomUUID().toString()

                        val existedUserId = transaction{
                            UserEntity.insert {
                                it[email] = existedEmail
                                it[username] = "testUsername"
                                it[password] = PasswordUtil.hashPassword(existedPassword)
                                it[role] = "USER"
                            }

                            UserEntity.select(
                                UserEntity.email eq existedEmail
                            ).map { it[UserEntity.id].toString() }.single()
                        }

                        val accessToken = generateAccessToken(tokenConfiguration, existedUserId)

                        val deleteResponse = client.delete("/users/non-userId") {
                            header("Authorization", "Bearer $accessToken")
                        }

                        assertEquals(HttpStatusCode.Forbidden, deleteResponse.status)

                    }
                }
            }
        }
    }
})