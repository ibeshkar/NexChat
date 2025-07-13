package com.artofelectronic.nexchat.domain.repository

import com.artofelectronic.nexchat.data.models.User

interface UserRepository {
    suspend fun createUserIfNotExists(user: User)
    suspend fun getAllUsers(): List<User>
}