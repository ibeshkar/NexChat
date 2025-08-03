package com.artofelectronic.nexchat.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.artofelectronic.nexchat.domain.model.Chat
import com.google.firebase.Timestamp

@Entity(tableName = "chats")
data class ChatEntity(
    @PrimaryKey val chatId: String,
    val lastMessage: String?,
    val lastSenderId: String?,
    val receiver: String?,
    val lastUpdated: Timestamp,
    val participants: List<String>
) {
    fun toDomain() =
        Chat(chatId, lastMessage, lastSenderId, receiver, lastUpdated, participants)

    companion object {
        fun fromDomain(chat: Chat) = ChatEntity(
            chat.chatId,
            chat.lastMessage,
            chat.lastSenderId,
            chat.receiver,
            chat.lastUpdated,
            chat.participants
        )
    }
}