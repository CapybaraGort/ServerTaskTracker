package org.example.route

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.example.data.database.dto.UserDTO
import org.example.data.database.entity.User
import org.example.data.database.mapper.toDto
import org.example.data.database.table.Categories
import org.example.data.database.table.Users
import org.example.data.response.IntResponse
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

fun Route.configureUserPostgreRoutes() {
    post("/users") {
        val newUser = call.receive<UserDTO>()

        transaction {
            val id = Users.insertAndGetId {
                it[name] = newUser.name
                it[email] = newUser.email
            }

            Categories.insert {
                it[userId] = id.value
                it[name] = "Дом"
            }
            Categories.insert {
                it[userId] = id.value
                it[name] = "Работа"
            }
        }

        call.respondSuccess(data = newUser, status = HttpStatusCode.Created)
    }
    get("/users/{email}") {
        val email = call.parameters["email"]
            ?: return@get call.respondError("Invalid email")

        try {
            val user = transaction {
                User.find { Users.email eq email }.singleOrNull()?.toDto()
            }

            call.respondSuccess(user)

        } catch (e: Exception) {
            call.respondError(e.message.toString(), HttpStatusCode.InternalServerError)
        }
    }
    get("/users/{id}") {
        val userId = call.parameters["id"]?.toIntOrNull()
            ?: return@get call.respondError("Invalid user id")

        val user = transaction {
            User.findById(userId)?.toDto()
        }

        if (user == null)
            call.respondError("user $userId not found", HttpStatusCode.NotFound)
        else
            call.respondSuccess(user)
    }
    get("/users") {
        val users = transaction {
            User.all().toList().map { it.toDto() }
        }
        call.respondSuccess(users)
    }
    put("/users/{id}") {
        val userId = call.parameters["id"]?.toIntOrNull()
            ?: return@put call.respondError("Invalid user id")

        val updatedUser = call.receive<UserDTO>()
        val updatedRows = transaction {
            Users.update({ Users.id eq userId}) {
                it[name] = updatedUser.name
                it[email] = updatedUser.email
            }
        }

        if(updatedRows > 0)
            call.respondSuccess(updatedUser)
        else
            call.respondError("User $userId not found", HttpStatusCode.NotFound)
    }
    delete("/users/{id}") {
        val userId = call.parameters["id"]?.toIntOrNull()
            ?: return@delete call.respondError("Invalid user id")

        val deletedRows = transaction {
            Users.deleteWhere { Users.id eq userId }
        }

        if(deletedRows > 0)
            call.respondSuccess(data = IntResponse(userId))
        else
            call.respondError("User $userId not found", HttpStatusCode.NotFound)
    }
}

@Serializable
data class ApiResponse<T>(
    val data: T? = null,
    val message: String? = null,
    val isError: Boolean = false
)

suspend inline fun <reified T> ApplicationCall.respondSuccess(
    data: T? = null,
    message: String? = null,
    status: HttpStatusCode = HttpStatusCode.OK
) = respond(status, ApiResponse(data = data, message = message, isError = false))

suspend fun ApplicationCall.respondError(
    message: String,
    status: HttpStatusCode = HttpStatusCode.BadRequest
) = respond(status, ApiResponse(data = null, message = message, isError = true))
