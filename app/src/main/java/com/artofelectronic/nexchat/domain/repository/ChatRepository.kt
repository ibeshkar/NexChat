package com.artofelectronic.nexchat.domain.repository

import com.artofelectronic.nexchat.data.models.Chat
import com.artofelectronic.nexchat.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getChatsForUser(userId: String): Flow<Resource<List<Chat>>>
    suspend fun createChatWith(userIds: List<String>): Chat
}