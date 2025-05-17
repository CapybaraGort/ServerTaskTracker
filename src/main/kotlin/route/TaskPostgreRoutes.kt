package org.example.route

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.example.data.database.dto.TaskDTO
import org.example.data.database.entity.Task
import org.example.data.database.mapper.toDto
import org.example.data.database.table.Tasks
import org.example.data.response.IntResponse
import org.example.data.response.TaskList
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

fun Route.configureTaskPostgreRoutes() {
    post("/tasks") {
        val newTask = call.receive<TaskDTO>()

        val taskId = transaction {
            Tasks.insertAndGetId {
                it[title] = newTask.title
                it[description] = newTask.description
                it[userId] = newTask.userId
                it[categoryId] = newTask.categoryId
                it[begin] = newTask.begin
                it[deadline] = newTask.deadline
                it[isCompleted] = newTask.isCompleted
            }
        }.value

        val task = transaction {
            Task.findById(taskId)
        }?.toDto() ?: return@post call.respondError("Can't find task by id: $taskId", HttpStatusCode.NotFound)

        call.respondSuccess(task, status = HttpStatusCode.Created)
    }
    get("/users/{user_id}/tasks") {
        val userId = call.parameters["user_id"]?.toIntOrNull()
            ?: return@get call.respondError("Invalid user ID")

        val tasks = transaction {
            Task.find { Tasks.userId eq userId }
                .map { it.toDto() }
        }

        call.respondSuccess(data = TaskList(tasks))
    }
    get("/users/tasks/{task_id}") {
        val taskId = call.parameters["task_id"]?.toIntOrNull()
            ?: return@get call.respondError("Invalid task ID")

        val task = transaction {
            Task.findById(taskId)?.toDto()
        }

        if (task != null) {
            call.respondSuccess(task, status = HttpStatusCode.Found)
        } else {
            call.respondError("task $taskId not found", HttpStatusCode.NotFound)
        }
    }
    put("/users/tasks/{id}") {
        val taskId = call.parameters["id"]?.toIntOrNull()
            ?: return@put call.respondError("Invalid task ID")
        val task = call.receive<TaskDTO>()

        val updRows = transaction {
            Tasks.update({ Tasks.id eq taskId }) {
                it[title] = task.title
                it[description] = task.description
                it[userId] = task.userId
                it[begin] = task.begin
                it[categoryId] = task.categoryId
                it[deadline] = task.deadline
                it[isCompleted] = task.isCompleted
            }
        }
        if(updRows > 0) {
            call.respondSuccess(task)
        } else {
            call.respondError(status = HttpStatusCode.NotFound, message = "task: $taskId not found")
        }
    }
    delete("/users/tasks/{id}") {
        val taskId = call.parameters["id"]?.toIntOrNull()
            ?: return@delete call.respondError("Invalid task ID")

        val deletedTasks = transaction {
            Tasks.deleteWhere { Tasks.id eq taskId }
        }

        if(deletedTasks > 0) {
            call.respondSuccess(IntResponse(taskId))
        } else {
            call.respondError(status = HttpStatusCode.NotFound, message = "task: $taskId not found")
        }
    }
    delete("/users/{user_id}/tasks") {
        val userId = call.parameters["user_id"]?.toIntOrNull()
            ?: return@delete call.respondError("Invalid user ID")

        val deletedTasks = transaction {
            Tasks.deleteWhere { Tasks.userId eq userId }
        }

        if(deletedTasks > 0) {
            call.respondSuccess(IntResponse(userId))
        } else {
            call.respondError(status = HttpStatusCode.NotFound, message = "user: $userId not found")
        }
    }
}