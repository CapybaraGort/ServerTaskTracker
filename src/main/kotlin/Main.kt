package org.example

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import org.example.data.converter.LocalDateTimeSerializer
import org.example.data.database.DatabaseConnector
import org.example.data.database.table.Tasks
import org.example.route.configureCategoryRoutes
import org.example.route.configureTaskPostgreRoutes
import org.example.route.configureUserPostgreRoutes
import org.example.route.configureWebSocketRoutes
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import kotlin.time.Duration.Companion.seconds

fun main() {
    embeddedServer(Netty, host = "0.0.0.0", port = 8080) {
        configureInstalls()

        DatabaseConnector().connect()

        routing {
            configureUserPostgreRoutes()
            configureTaskPostgreRoutes()
            configureWebSocketRoutes()
            configureCategoryRoutes()
        }
    }.start(wait = true)
}

fun Application.configureInstalls() {
    install(ContentNegotiation) {
        json(Json {
            encodeDefaults = true
            ignoreUnknownKeys = true
            isLenient = true
            serializersModule = SerializersModule {
                contextual(LocalDateTime::class, LocalDateTimeSerializer)
            }
        })
    }
    install(WebSockets) {
        pingPeriod = 2.seconds
    }
}