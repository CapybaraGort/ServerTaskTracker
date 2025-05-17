package org.example.data.database.entity

import org.example.data.database.table.Users
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class User(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<User>(Users)

    val name by Users.name
    val email by Users.email
}