package org.example.data.response

import kotlinx.serialization.Serializable
import org.example.data.database.dto.TaskDTO

@Serializable
data class TaskList(val tasks: List<TaskDTO>)