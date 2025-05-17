package org.example.data.database.entity

import org.example.data.database.table.Categories
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Category(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<Category>(Categories)

    val userId by Categories.userId
    val name by Categories.name
}