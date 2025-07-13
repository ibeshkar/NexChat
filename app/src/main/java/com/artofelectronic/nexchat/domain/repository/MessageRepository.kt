package com.artofelectronic.nexchat.domain.repository

import android.net.Uri
import com.artofelectronic.nexchat.data.models.Message
import com.artofelectronic.nexchat.utils.Resource
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    fun listenToMessages(chatId: String): Flow<Resource<List<Message>>>
    suspend fun sendMessage(chatId: String, message: Message)
    suspend fun uploadImageAndSendMessage(chatId: String, senderId: String, imageUri: Uri)
}