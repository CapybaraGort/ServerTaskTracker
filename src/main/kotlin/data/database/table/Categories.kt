package org.example.data.database.table

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object Categories : IntIdTable("task_tracker.categories") {
    val userId = reference("user_id", Users, onDelete = ReferenceOption.CASCADE).index()
    val name = varchar("name", 64)
}