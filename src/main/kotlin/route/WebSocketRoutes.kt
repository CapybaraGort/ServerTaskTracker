package org.example.route

import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*

fun Route.configureWebSocketRoutes() {
    webSocket("/updates") {
        send("Сервер: Привет! Вы подключены.")

        for (frame in incoming) {
            if (frame is Frame.Text) {
                val clientMessage = frame.readText()
                println("Получено от клиента: $clientMessage")
            }
        }
    }
}