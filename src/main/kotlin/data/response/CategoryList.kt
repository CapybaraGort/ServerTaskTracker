package org.example.data.response

import kotlinx.serialization.Serializable
import org.example.data.database.dto.CategoryDTO

@Serializable
data class CategoryList(val categories: List<CategoryDTO>)
