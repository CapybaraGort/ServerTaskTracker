package org.example.data.database.entity

import org.example.data.database.table.Tasks
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Task(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<Task>(Tasks)

    var title by Tasks.title
    var description by Tasks.description
    var userId by Tasks.userId
    var deadline by Tasks.deadline
    var begin by Tasks.begin
    val categoryId by Tasks.categoryId
    var isCompleted by Tasks.isCompleted
}