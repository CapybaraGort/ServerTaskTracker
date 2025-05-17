package org.example.data.response

import kotlinx.serialization.Serializable
import org.example.data.database.dto.UserDTO

@Serializable
data class UserList(val users: List<UserDTO>)