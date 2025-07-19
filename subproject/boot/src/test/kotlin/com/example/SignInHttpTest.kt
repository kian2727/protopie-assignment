package com.example

import com.example.TestDataHelper.generateEmail
import com.example.protopie.domain.PasswordUtil
import com.example.protopie.infrastructure.DatabaseConfiguration
import com.example.protopie.infrastructure.UserEntity
import com.example.protopie.presentation.dto.SignInRequest
import io.kotest.core.spec.style.FreeSpec
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import junit.framework.TestCase.assertEquals
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class SignInHttpTest:FreeSpec( {
    lateinit var databaseConfiguration: DatabaseConfiguration
    beforeTest {
        databaseConfiguration = HttpTestEnvironment.global.databaseConfiguration
    }
    afterEach {
        runCustomTestApplication(databaseConfiguration) {
            transaction {
                UserEntity.deleteAll()
            }
        }
    }
    "회원 로그인 테스트 " - {
        val existedEmail = generateEmail()
        val existedPassword = UUID.randomUUID().toString()

        "성공 케이스" {
            runCustomTestApplication(databaseConfiguration) {
                transaction{
                    UserEntity.insert {
                        it[email] = existedEmail
                        it[username] = "testUsername"
                        it[password] = PasswordUtil.hashPassword(existedPassword)
                        it[role] = "USER"
                    }
                }

                val response = client.post("/signin") {
                    contentType(ContentType.Application.Json)

                    setBody(
                        SignInRequest(
                            email = existedEmail ,
                            password = existedPassword
                        )
                    )
                }

                assertEquals(HttpStatusCode.OK, response.status)
            }
        }
        "실패 케이스" - {
            "존재 하지 않는 이메일로 로그인 시도" {
                runCustomTestApplication(databaseConfiguration){
                    val response = client.post("/signin") {
                        contentType(ContentType.Application.Json)

                        setBody(
                            SignInRequest(
                                email = generateEmail() ,
                                password = UUID.randomUUID().toString()
                            )
                        )
                    }

                    assertEquals(HttpStatusCode.NotFound, response.status)
                }
            }
            "이메일은 존재하나 password가 맞지 않는경우" {
                runCustomTestApplication(databaseConfiguration){
                    transaction{
                        UserEntity.insert {
                            it[email] = existedEmail
                            it[username] = "testUsername"
                            it[password] = PasswordUtil.hashPassword(existedPassword)
                            it[role] = "USER"
                        }
                    }
                    val response = client.post("/signin") {
                        contentType(ContentType.Application.Json)

                        setBody(
                            SignInRequest(
                                email = existedEmail ,
                                password = UUID.randomUUID().toString()
                            )
                        )
                    }

                    assertEquals(HttpStatusCode.NotFound, response.status)
                }
            }
            "이메일과 패스워드는 일치하나, 탈퇴한 유저인 경우" {
                runCustomTestApplication(databaseConfiguration){
                    transaction{
                        UserEntity.insert {
                            it[email] = existedEmail
                            it[username] = "testUsername"
                            it[password] = PasswordUtil.hashPassword(existedPassword)
                            it[role] = "USER"
                            it[isActive] = false
                        }
                    }
                    val response = client.post("/signin") {
                        contentType(ContentType.Application.Json)
                        setBody(
                            SignInRequest(
                                email = existedEmail ,
                                password = PasswordUtil.hashPassword(existedPassword)
                            )
                        )
                    }

                    assertEquals(HttpStatusCode.NotFound, response.status)
                }
            }
        }
    }
})