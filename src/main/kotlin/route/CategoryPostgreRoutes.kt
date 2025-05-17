package org.example.route

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import org.example.data.database.dto.CategoryDTO
import org.example.data.database.entity.Category
import org.example.data.database.entity.Task
import org.example.data.database.mapper.toDto
import org.example.data.database.table.Categories
import org.example.data.response.CategoryList
import org.example.data.response.IntResponse
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.lang.Exception

fun Route.configureCategoryRoutes() {
    post("/users/categories") {
        try {
            val newCategory = call.receive<CategoryDTO>()

            val categoryId = transaction {
                Categories.insertAndGetId {
                    it[userId] = newCategory.userId
                    it[name] = newCategory.name
                }
            }.value

            val category = transaction {
                Category.findById(categoryId)
            }?.toDto() ?: return@post call.respondError(message = "Category: $categoryId not found", status = HttpStatusCode.NotFound)

            call.respondSuccess(data = category, status = HttpStatusCode.Created)
        }catch (e: Exception) {
            call.respondError(message = e.message.toString())
        }

    }

    get("/users/{user_id}/categories") {
        val userId = call.parameters["user_id"]?.toIntOrNull()
            ?: return@get call.respondError("Invalid user id")

        val categories = transaction {
            Category.find { Categories.userId eq userId }
                .map { it.toDto() }
        }

        call.respondSuccess(data = CategoryList(categories))
    }

    put("/users/categories/{category_id}") {
        val categoryId = call.parameters["category_id"]?.toIntOrNull()
            ?: return@put call.respondError(message = "Invalid category id")

        val category = call.receive<CategoryDTO>()

        val updCategories = transaction {
            Categories.update({ Categories.id eq categoryId }) {
                it[userId] = category.userId
                it[name] = category.name
            }
        }

        val updatedCategory = transaction {
            Category.findById(categoryId)
        } ?: return@put call.respondError(message = "Category: $categoryId not found", status = HttpStatusCode.NotFound)

        if(updCategories > 0) {
            call.respondSuccess(data = updatedCategory.toDto())
        } else {
            call.respondError(message = "Category: $categoryId not updated")
        }
    }

    delete("/users/categories/{category_id}") {
        val categoryId = call.parameters["category_id"]?.toIntOrNull()
            ?: return@delete call.respondError("Invalid category id")

        val deletedCategory = transaction {
            Categories.deleteWhere { Categories.id eq categoryId }
        }

        if(deletedCategory > 0) {
            call.respondSuccess(IntResponse(categoryId))
        } else {
            call.respondError(message = "Category: $categoryId not found", status = HttpStatusCode.NotFound)
        }
    }
}

