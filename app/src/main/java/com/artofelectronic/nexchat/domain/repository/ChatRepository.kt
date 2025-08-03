package com.artofelectronic.nexchat.domain.repository

import com.artofelectronic.nexchat.domain.model.Chat
import com.artofelectronic.nexchat.domain.model.Message
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun observeChats(): Flow<List<Chat>>
    fun observeChat(chatId: String): Flow<Chat?>
    suspend fun fetchChatsOnceIfNeeded(userId: String)
    fun startRealtimeSync(userId: String): ListenerRegistration
    suspend fun insertChat(chat: Chat)
    suspend fun retryPendingUpdates()
    suspend fun refreshChatsFromFirebase(userId: String)
    fun observeMessages(chatId: String): Flow<List<Message>>
    suspend fun sendMessage(message: Message, receiver: String)
    fun startMessageListener(chatId: String): ListenerRegistration
}