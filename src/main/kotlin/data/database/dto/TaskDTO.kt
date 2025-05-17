package org.example.data.database.dto

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class TaskDTO(
    val id: Int? = null,
    val title: String,
    val description: String?,
    @SerialName("user_id") val userId: Int,
    @SerialName("category_id") val categoryId: Int,
    @Contextual val deadline: LocalDateTime,
    @Contextual val begin: LocalDateTime,
    @SerialName("is_completed") val isCompleted: Boolean
)