package org.example.data.database.table

import org.jetbrains.exposed.dao.id.IntIdTable

object Users : IntIdTable("task_tracker.users") {
    val name = varchar("name", 48)
    val email = varchar("email", 64).uniqueIndex()
}