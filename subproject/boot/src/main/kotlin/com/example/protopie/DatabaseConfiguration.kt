package com.example.protopie

import com.typesafe.config.Config

data class DatabaseConfiguration(
    val host:String,
    val port:Int,
    val database:String,
    val username: String,
    val password: String
){
    companion object{
        fun loadConfiguration(config:Config):DatabaseConfiguration=
            DatabaseConfiguration(
                host = config.getString("database.postgresql.host"),
                port = config.getInt("database.postgresql.port"),
                database = config.getString("database.postgresql.database"),
                username = config.getString("database.postgresql.username"),
                password = config.getString("database.postgresql.password")
            )
    }

    val jdbcUrl = "jdbc:postgresql://$host:$port/$database"
}
