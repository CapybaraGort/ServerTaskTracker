package org.example.data.database

import io.github.cdimascio.dotenv.Dotenv
import org.jetbrains.exposed.sql.Database

class DatabaseConnector {
    fun connect() {
        val dotenv = Dotenv.configure()
            .directory(System.getProperty("user.dir"))
            .filename(".env")
            .load()

        val url = dotenv["DB_URL"]
        val user = dotenv["DB_USER"]
        val password = dotenv["DB_PASSWORD"]

        Database.connect(
            url = url,
            driver = "org.postgresql.Driver",
            user = user,
            password = password
        )
    }
}