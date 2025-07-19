package com.example

import com.example.TestDataHelper.generateAccessToken
import com.example.TestDataHelper.generateEmail
import com.example.protopie.domain.PasswordUtil
import com.example.protopie.domain.jwt.TokenConfiguration
import com.example.protopie.infrastructure.DatabaseConfiguration
import com.example.protopie.infrastructure.UserEntity
import com.example.protopie.presentation.dto.UserResponse
import io.kotest.core.spec.style.FreeSpec
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import junit.framework.TestCase.assertEquals
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

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