package org.example.data.database.table

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.datetime

object Tasks : IntIdTable("task_tracker.tasks") {
    val title = varchar("title", 512)
    val description = text("description").nullable()
    val userId = reference("user_id", Users).index()
    val categoryId = reference("category_id", Categories, onDelete = ReferenceOption.CASCADE).index()
    val deadline = datetime("deadline")
    val begin = datetime("begin")
    val isCompleted = bool("is_completed")
}