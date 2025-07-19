package com.example
import com.example.protopie.infrastructure.DatabaseConfiguration
import com.example.protopie.infrastructure.UserEntity
import com.example.protopie.presentation.dto.SignUpRequest
import io.kotest.core.spec.style.FreeSpec
import io.ktor.client.request.*
import io.ktor.http.*
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.assertEquals

class SignUpHttpTest:FreeSpec ({

    lateinit var databaseConfiguration: DatabaseConfiguration
    beforeTest {
        databaseConfiguration = HttpTestEnvironment.global.databaseConfiguration
    }

    "회원 가입 테스트" - {
        "성공 케이스" {
            runCustomTestApplication(databaseConfiguration){
                val response = client.post("/signup") {
                    contentType(ContentType.Application.Json)

                    val request = SignUpRequest(
                        TestDataHelper.generateEmail(),
                        "testUsername",
                        "password",
                        null
                    )

                    setBody(request)
                }
                assertEquals(HttpStatusCode.NoContent, response.status)
            }
        }
        "실패 케이스" - {
            "이메일 중복 실패"-{
                runCustomTestApplication(databaseConfiguration){
                    val existedEmail = TestDataHelper.generateEmail()
                    transaction {
                        UserEntity.insert {
                            it[email] = existedEmail
                            it[username] = "testUsername"
                            it[password] = "<PASSWORD>"
                            it[role] = "USER"
                        }
                    }

                    val response = client.post("/signup") {
                        contentType(ContentType.Application.Json)

                        val request = SignUpRequest(
                            existedEmail,
                            "testUsername",
                            "password",
                        )

                        setBody(request)
                    }

                    assertEquals(HttpStatusCode.Conflict, response.status)
                }
            }
        }
    }
})

