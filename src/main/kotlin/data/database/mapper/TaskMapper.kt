package org.example.data.database.mapper

import org.example.data.database.dto.TaskDTO
import org.example.data.database.entity.Task

fun Task.toDto() = TaskDTO(
    id = id.value,
    title = title,
    description = description,
    userId = userId.value,
    deadline = deadline,
    begin = begin,
    categoryId = categoryId.value,
    isCompleted = isCompleted
)