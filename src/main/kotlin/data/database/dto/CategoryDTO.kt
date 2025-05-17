package org.example.data.database.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryDTO(
    val id: Int? = null,

    @SerialName("user_id") val userId: Int,
    @SerialName("name") val name: String
)
