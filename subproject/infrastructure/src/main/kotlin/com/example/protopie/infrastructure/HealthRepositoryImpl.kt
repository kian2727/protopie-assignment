package com.example.protopie.infrastructure

import com.example.protopie.domain.HealthRepository
import org.jetbrains.exposed.sql.transactions.transaction

class HealthRepositoryImpl():HealthRepository {
    override fun check() {
        transaction {
            exec("SELECT 1")
        }
    }
}