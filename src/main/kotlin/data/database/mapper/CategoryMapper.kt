package org.example.data.database.mapper

import org.example.data.database.dto.CategoryDTO
import org.example.data.database.entity.Category

fun Category.toDto(): CategoryDTO {
    return CategoryDTO(
        id = id.value,
        userId = userId.value,
        name = name
    )
}