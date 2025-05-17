package org.example.data.database.mapper

import org.example.data.database.dto.UserDTO
import org.example.data.database.entity.User

fun User.toDto() = UserDTO(
    id = id.value,
    name = name,
    email = email
)